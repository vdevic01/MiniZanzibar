package com.example.MiniZanzibarBack.dto;

import java.util.List;

public class AccessibleFilesResponse {
    private String namespace;
    private String user_id;
    private List<AccessibleObject> objects;

    // Getters and setters
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<AccessibleObject> getObjects() {
        return objects;
    }

    public void setObjects(List<AccessibleObject> objects) {
        this.objects = objects;
    }

    public static class AccessibleObject {
        private String object_id;
        private String relation;

        // Getters and setters
        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }
    }
}
