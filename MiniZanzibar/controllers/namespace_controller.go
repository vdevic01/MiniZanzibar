package controllers

import (
	"github.com/gin-gonic/gin"
)

type NamespaceController struct {
	BaseController
}

func NewNamespaceController(apiRoutePrefix string) NamespaceController {
	controller := NamespaceController{
		BaseController: InitController(apiRoutePrefix, "/namespace"),
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

}
