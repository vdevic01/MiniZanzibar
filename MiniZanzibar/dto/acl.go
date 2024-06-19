package dto

type AclTupleDto struct {
	Namespace string `json:"namespace" binding:"required"`
	ObjectId  string `json:"object_id" binding:"required"`
	Relation  string `json:"relation" binding:"required"`
	UserId    string `json:"user_id" binding:"required"`
}

type AccessGrant struct {
	Allowed bool `json:"allowed"`
}
