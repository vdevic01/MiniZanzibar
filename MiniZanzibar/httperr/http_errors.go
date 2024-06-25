package httperr

import (
	"fmt"
	"net/http"
)

type HttpError struct {
	StatusCode int
	Message    string
}

func (e *HttpError) Error() string {
	return fmt.Sprintf("status %d: %s", e.StatusCode, e.Message)
}

func NewNotFoundError(message string) *HttpError {
	return &HttpError{
		StatusCode: http.StatusNotFound,
		Message:    message,
	}
}
func BadRequestError(message string) *HttpError {
	return &HttpError{
		StatusCode: http.StatusBadRequest,
		Message:    message,
	}
}
