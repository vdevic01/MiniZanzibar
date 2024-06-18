package controllers

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/httperr"
	"MiniZanzibar/services"
	"github.com/gin-gonic/gin"
	"net/http"
)

type AclController struct {
	BaseController
	AclService *services.AclService
}

func NewAclController(apiRoutePrefix string, aclService *services.AclService) AclController {
	controller := AclController{
		BaseController: InitController(apiRoutePrefix, "/acl"),
		AclService:     aclService,
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
	var aclTupleDto dto.AclTupleDto

	if err := context.ShouldBindJSON(&aclTupleDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := controller.AclService.SaveAcl(aclTupleDto)
	if err != nil {
		if httpError, ok := err.(*httperr.HttpError); ok {
			context.JSON(httpError.StatusCode, gin.H{"error": httpError.Message})
		} else {
			context.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
		}
	}

	context.JSON(http.StatusNoContent, gin.H{})
}
