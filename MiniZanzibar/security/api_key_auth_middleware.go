package security

import (
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

const apiKeyHeader = "X-API-KEY"

func ApiKeyAuthMiddleware() gin.HandlerFunc {
	return func(context *gin.Context) {
		apiKey := context.GetHeader(apiKeyHeader)
		if apiKey == "" {
			context.JSON(http.StatusUnauthorized, gin.H{"error": "API key required"})
			context.Abort()
			return
		}

		if apiKey != os.Getenv("API_KEY") {
			context.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid API key"})
			context.Abort()
			return
		}

		context.Next()
	}
}
