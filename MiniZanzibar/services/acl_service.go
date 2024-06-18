package services

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/httperr"
	"MiniZanzibar/model"
	"MiniZanzibar/utils"
	"log"
	"os"

	"github.com/hashicorp/consul/api"
	"github.com/syndtr/goleveldb/leveldb"
)

type AclService struct {
	ConsulDbClient *api.Client
}

func contains(slice *[]string, value string) bool {
	for _, item := range *slice {
		if item == value {
			return true
		}
	}
	return false
}

func buildRelationTupleKey(namespace string, objectId string, relation string, userId string) string {
	return namespace + ":" + objectId + "#" + relation + "@" + userId
}

func (aclService *AclService) SaveAcl(dto dto.AclTupleDto) error {
	pair, _, err := aclService.ConsulDbClient.KV().Get(dto.Namespace, nil)
	if err != nil {
		log.Fatal(err)
	}
	if pair == nil {
		return httperr.NewNotFoundError("Namespace with name " + dto.Namespace + " not found")
	}
	namespace := model.NewNamespaceFromBytes(pair.Value)
	val, exists := namespace.Relations[dto.Relation]
	if !exists {
		return httperr.NewNotFoundError("Relation with name " + dto.Relation + " not found")
	}
	if len(val) != 0 && !contains(&val, "this") {
		return httperr.BadRequestError("Relation " + dto.Relation + " cannot be granted explicitly")
	}
	graph := utils.NewNamespaceGraph()
	for k, v := range namespace.Relations {
		graph.InsertNode(k)
		for _, rel := range v {
			if rel == "this" {
				continue
			}
			graph.InsertNode(rel)
			_ = graph.InsertConnection(rel, k)
		}
	}
	queue := utils.NewQueue[string]()
	queue.Enqueue(dto.Relation)
	db, err := leveldb.OpenFile(os.Getenv("LEVELDB_PATH"), nil)
	if err != nil {
		panic(err)
	}
	for !queue.IsEmpty() {
		el, _ := queue.Dequeue()
		relationTupleKey := buildRelationTupleKey(dto.Namespace, dto.ObjectId, el, dto.UserId)
		err := db.Put([]byte(relationTupleKey), []byte{0x0}, nil)
		if err != nil {
			panic(err)
		}
		node := graph.Nodes[el]
		for _, child := range node.Children { // TODO Fix infinite loop
			queue.Enqueue(child.RelationName)
		}

	}
	_ = db.Close()
	return nil
}
