package services

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/model"
	"fmt"

	"github.com/hashicorp/consul/api"
)

type NamespaceService struct {
	ConsulDbClient *api.Client
}

func (service *NamespaceService) SaveNamespace(dto dto.NamespaceDto) error {
	namespace := model.NewNamespace(dto)
	fmt.Printf("%+v\n", namespace)

	kvClient := service.ConsulDbClient.KV()
	value, err := namespace.Encode()
	if err != nil {
		return err
	}
	pair := &api.KVPair{Key: namespace.Name, Value: value}

	_, err = kvClient.Put(pair, nil)
	if err != nil {
		return err
	}
	return nil
}
