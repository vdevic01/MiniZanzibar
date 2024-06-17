package controllers

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
