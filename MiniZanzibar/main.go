package main

import (
	"MiniZanzibar/controllers"
	"github.com/gin-gonic/gin"
)

const (
	apiRoute = "/api"
)

func main() {
	router := gin.Default()

	namespaceController := controllers.NewNamespaceController(apiRoute)

	namespaceController.RegisterRoutes(router)

	router.Run(":8080")
}
