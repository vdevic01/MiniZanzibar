package controllers

import (
	"MiniZanzibar/dto"
	"MiniZanzibar/services"
	"net/http"

	"github.com/gin-gonic/gin"
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
	router.POST(controller.route+"/check", controller.CheckAcl)
	router.POST(controller.route, controller.SaveAcl)
	router.DELETE(controller.route, controller.RemoveAcl)
}

func (controller *AclController) CheckAcl(context *gin.Context) {
	var aclTupleDto dto.AclTupleDto

	if err := context.ShouldBindJSON(&aclTupleDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	response, err := controller.AclService.CheckAccess(aclTupleDto)
	controller.handleError(context, err)

	context.JSON(http.StatusOK, response)
}

func (controller *AclController) SaveAcl(context *gin.Context) {
	var aclTupleDto dto.AclTupleDto

	if err := context.ShouldBindJSON(&aclTupleDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := controller.AclService.SaveAcl(aclTupleDto)
	controller.handleError(context, err)

	context.JSON(http.StatusNoContent, gin.H{})
}

func (controller *AclController) RemoveAcl(context *gin.Context) {
	var aclTupleDto dto.AclTupleDto

	if err := context.ShouldBindJSON(&aclTupleDto); err != nil {
		context.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err := controller.AclService.RemoveAcl(aclTupleDto)
	controller.handleError(context, err)

	context.JSON(http.StatusNoContent, gin.H{})
}
