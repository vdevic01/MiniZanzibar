package controllers

import (
	"github.com/gin-gonic/gin"
)

type AclController struct {
	BaseController
}

func NewAclController(apiRoutePrefix string) AclController {
	controller := AclController{
		BaseController: InitController(apiRoutePrefix, "/acl"),
	}
	return controller
}

func (controller *AclController) RegisterRoutes(router *gin.Engine) {
	router.GET(controller.route+"/check", controller.CheckAcl)
	router.POST(controller.route, controller.SaveAcl)
}

func (controller *AclController) CheckAcl(context *gin.Context) {

}

func (controller *AclController) SaveAcl(context *gin.Context) {

}
