package main

import (
	"MiniZanzibar/controllers"
	"MiniZanzibar/services"
	"log"

	"github.com/gin-gonic/gin"
	"github.com/hashicorp/consul/api"
	"github.com/joho/godotenv"
)

const (
	apiRoute = "/api"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatalf(err.Error())
	}
	router := gin.Default()

	consulDbClient, err := api.NewClient(api.DefaultConfig())
	if err != nil {
		log.Fatal(err)
	}
	namespaceService := services.NamespaceService{
		ConsulDbClient: consulDbClient,
	}
	namespaceController := controllers.NewNamespaceController(apiRoute, &namespaceService)
	namespaceController.RegisterRoutes(router)

	aclService := services.AclService{
		ConsulDbClient: consulDbClient,
	}
	aclController := controllers.NewAclController(apiRoute, &aclService)
	aclController.RegisterRoutes(router)

	router.Run(":8080")
}
