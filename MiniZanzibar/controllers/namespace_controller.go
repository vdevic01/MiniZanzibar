package controllers

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/services"
	"net/http"

	"github.com/gin-gonic/gin"
)

type NamespaceController struct {
	BaseController
	NamespaceService *services.NamespaceService
}

func NewNamespaceController(apiRoutePrefix string, namespaceService *services.NamespaceService) NamespaceController {
	controller := NamespaceController{
		BaseController:   InitController(apiRoutePrefix, "/namespace"),
		NamespaceService: namespaceService,
	}
	return controller
}

func (controller *NamespaceController) RegisterRoutes(router *gin.Engine) {
	router.POST(controller.route, controller.SaveNamespace)
}

func (controller *NamespaceController) SaveNamespace(context *gin.Context) {
	var namespaceDto dto.NamespaceDto

	if err := context.ShouldBindJSON(&namespaceDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := controller.NamespaceService.SaveNamespace(namespaceDto)
	controller.handleError(context, err)

	context.JSON(http.StatusNoContent, gin.H{})
}
