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

type UserOwnership struct {
	Namespace string               `json:"namespace"`
	UserId    string               `json:"user_id"`
	Objects   []ObjectRelationPair `json:"objects"`
}

type ObjectRelationPair struct {
	ObjectId string `json:"object_id"`
	Relation string `json:"relation"`
}
