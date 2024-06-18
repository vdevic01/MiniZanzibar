package model

import (
	"MiniZanzibar/dto"
	"encoding/json"
	"fmt"
	"log"
)

type Namespace struct {
	Name      string              `json:"name"`
	Relations map[string][]string `json:"relations"`
}

func NewNamespace(dto dto.NamespaceDto) Namespace {
	relations := make(map[string][]string)
	for k, v := range dto.Relations {
		relations[k] = v
	}
	return Namespace{
		Name:      dto.Name,
		Relations: relations,
	}
}

func (namespace *Namespace) Encode() []byte {
	data, err := json.Marshal(namespace)
	if err != nil {
		log.Fatal(err)
	}
	return data
}

func NewNamespaceFromBytes(data []byte) Namespace {
	var namespace Namespace
	fmt.Println(string(data[:]))
	if err := json.Unmarshal(data, &namespace); err != nil {
		panic(err)
	}

	return namespace
}
