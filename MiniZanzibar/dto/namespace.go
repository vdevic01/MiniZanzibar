package dto

type NamespaceDto struct {
	Name      string              `json:"name" binding:"required"`
	Relations map[string][]string `json:"relations"  binding:"required"`
}
