package main

import (
	"MiniZanzibar/controllers"
	"MiniZanzibar/security"
	"MiniZanzibar/services"
	"log"
	"os"
	"strconv"

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

	secured, err := strconv.ParseBool(os.Getenv("USE_API_KEY"))
	if err != nil {
		log.Fatalf(err.Error())
	}

	router := gin.Default()
	if secured {
		router.Use(security.ApiKeyAuthMiddleware())
	}

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
