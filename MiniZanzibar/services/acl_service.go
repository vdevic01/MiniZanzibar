package services

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/httperr"
	"MiniZanzibar/model"
	"MiniZanzibar/utils"
	"bytes"
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/hashicorp/consul/api"
	"github.com/syndtr/goleveldb/leveldb"
)

func (aclService *AclService) CheckAccess(aclTupleDto dto.AclTupleDto) (dto.AccessGrant, error) {
	pair, _, err := aclService.ConsulDbClient.KV().Get(aclTupleDto.Namespace, nil)
	if err != nil {
		return dto.AccessGrant{}, err
	}
	if pair == nil {
		return dto.AccessGrant{
			Allowed: false,
		}, nil
	}
	namespace, err := model.NewNamespaceFromBytes(pair.Value)
	if err != nil {
		return dto.AccessGrant{}, err
	}
	rel, exists := namespace.Relations[aclTupleDto.Relation]
	if !exists {
		return dto.AccessGrant{
			Allowed: false,
		}, nil
	}
	queue := utils.NewQueue[string]()
	checklist := make(map[string]struct{})
	for _, union := range rel {
		if union != "this" {
			queue.Enqueue(union)
		} else {
			queue.Enqueue(aclTupleDto.Relation)
		}
	}

	db, err := leveldb.OpenFile(os.Getenv("LEVELDB_PATH"), nil)
	if err != nil {
		return dto.AccessGrant{}, err
	}
	defer db.Close()
	for !queue.IsEmpty() {
		union, _ := queue.Dequeue()
		relationTupleKey := buildRelationTupleKey(aclTupleDto.Namespace, aclTupleDto.ObjectId, union, aclTupleDto.UserId)
		_, err = db.Get([]byte(relationTupleKey), nil)
		if err == nil {
			return dto.AccessGrant{
				Allowed: true,
			}, nil
		}
		checklist[union] = struct{}{}
		rel, exists = namespace.Relations[union]
		if exists {
			for _, newUnion := range rel {

				if newUnion == "this" {
					newUnion = union
				}
				if _, exists := checklist[newUnion]; !exists {
					queue.Enqueue(newUnion)
				}
			}
		}
	}
	return dto.AccessGrant{
		Allowed: false,
	}, nil
}

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
	return namespace + ":" + userId + "#" + relation + "@" + objectId
}

func (aclService *AclService) GetObjectsForUserAndNamespace(namespace string, userId string) (dto.UserOwnership, error) {
	startKey := []byte(fmt.Sprintf("%s:%s", namespace, userId))
	endKey := []byte(fmt.Sprintf("%s:%s\xff", namespace, userId))

	db, err := leveldb.OpenFile(os.Getenv("LEVELDB_PATH"), nil)
	if err != nil {
		return dto.UserOwnership{}, err
	}
	defer db.Close()

	iterator := db.NewIterator(nil, nil)
	defer iterator.Release()

	output := dto.UserOwnership{
		Namespace: namespace,
		UserId:    userId,
		Objects:   make([]dto.ObjectRelationPair, 0),
	}

	for iterator.Seek(startKey); iterator.Valid(); iterator.Next() {
		key := iterator.Key()
		if !bytes.HasPrefix(key, startKey) || bytes.Compare(key, endKey) >= 0 {
			break
		}
		tokens := strings.Split(string(key), "#")
		tokens = strings.Split(tokens[1], "@")
		orPair := dto.ObjectRelationPair{
			ObjectId: tokens[1],
			Relation: tokens[0],
		}
		output.Objects = append(output.Objects, orPair)
	}

	return output, nil
}

func (aclService *AclService) SaveAcl(dto dto.AclTupleDto) error {
	pair, _, err := aclService.ConsulDbClient.KV().Get(dto.Namespace, nil)
	if err != nil {
		log.Fatal(err)
	}
	if pair == nil {
		return httperr.NewNotFoundError("Namespace with name " + dto.Namespace + " not found")
	}
	namespace, err := model.NewNamespaceFromBytes(pair.Value)
	if err != nil {
		return err
	}
	val, exists := namespace.Relations[dto.Relation]
	if !exists {
		return httperr.NewNotFoundError("Relation with name " + dto.Relation + " not found")
	}
	if len(val) != 0 && !contains(&val, "this") {
		return httperr.BadRequestError("Relation " + dto.Relation + " cannot be granted explicitly")
	}
	relationTupleKey := buildRelationTupleKey(dto.Namespace, dto.ObjectId, dto.Relation, dto.UserId)
	db, err := leveldb.OpenFile(os.Getenv("LEVELDB_PATH"), nil)
	if err != nil {
		return err
	}
	defer db.Close()
	err = db.Put([]byte(relationTupleKey), []byte{0x0}, nil)
	if err != nil {
		return err
	}

	return nil
}

func (aclService *AclService) RemoveAcl(dto dto.AclTupleDto) error {
	pair, _, err := aclService.ConsulDbClient.KV().Get(dto.Namespace, nil)
	if err != nil {
		log.Fatal(err)
	}
	if pair == nil {
		return httperr.NewNotFoundError("Namespace with name " + dto.Namespace + " not found")
	}
	namespace, err := model.NewNamespaceFromBytes(pair.Value)
	if err != nil {
		return err
	}
	_, exists := namespace.Relations[dto.Relation]
	if !exists {
		return httperr.NewNotFoundError("Relation with name " + dto.Relation + " not found")
	}

	relationTupleKey := buildRelationTupleKey(dto.Namespace, dto.ObjectId, dto.Relation, dto.UserId)
	db, err := leveldb.OpenFile(os.Getenv("LEVELDB_PATH"), nil)
	if err != nil {
		return err
	}
	defer db.Close()
	err = db.Delete([]byte(relationTupleKey), nil)
	if err != nil {
		return err
	}

	return nil
}
