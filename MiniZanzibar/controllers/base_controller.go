package controllers

import (
	"MiniZanzibar/httperr"
	"net/http"

	"github.com/gin-gonic/gin"
)

type BaseController struct {
	controllerRoutePrefix string
	apiRoutePrefix        string
	route                 string
}

func InitController(apiRoutePrefix string, controllerRoutePrefix string) BaseController {
	controller := BaseController{
		controllerRoutePrefix: controllerRoutePrefix,
		apiRoutePrefix:        apiRoutePrefix,
	}
	controller.route = controller.apiRoutePrefix + controller.controllerRoutePrefix
	return controller
}

func (baseController *BaseController) handleError(context *gin.Context, err error) {
	if err != nil {
		if httpError, ok := err.(*httperr.HttpError); ok {
			context.JSON(httpError.StatusCode, gin.H{"error": httpError.Message})
		} else {
			context.JSON(http.StatusInternalServerError, gin.H{"error": "Internal server error"})
		}
	}
}
