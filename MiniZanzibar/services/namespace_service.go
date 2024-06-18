package services

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/model"
	"fmt"
	"github.com/hashicorp/consul/api"
	"log"
)

type NamespaceService struct {
	ConsulDbClient *api.Client
}

func (service *NamespaceService) SaveNamespace(dto dto.NamespaceDto) {
	namespace := model.NewNamespace(dto)
	fmt.Printf("%+v\n", namespace)

	kvClient := service.ConsulDbClient.KV()
	value := namespace.Encode()
	pair := &api.KVPair{Key: namespace.Name, Value: value}

	_, err := kvClient.Put(pair, nil)
	if err != nil {
		log.Fatal(err)
	}
}
