package controllers

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/services"
	"github.com/gin-gonic/gin"
	"net/http"
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
	router.GET(controller.route+"/:namespace_id", controller.GetNamespace)
	router.GET(controller.route+"/all", controller.GetAllNamespace)
	router.POST(controller.route, controller.SaveNamespace)
}

func (controller *NamespaceController) GetNamespace(context *gin.Context) {
}

func (controller *NamespaceController) GetAllNamespace(context *gin.Context) {

}

func (controller *NamespaceController) SaveNamespace(context *gin.Context) {
	var namespaceDto dto.NamespaceDto

	if err := context.ShouldBindJSON(&namespaceDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	controller.NamespaceService.SaveNamespace(namespaceDto)

	context.JSON(http.StatusNoContent, gin.H{})
}
