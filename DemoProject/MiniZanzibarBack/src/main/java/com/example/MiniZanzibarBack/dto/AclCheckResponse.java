package com.example.MiniZanzibarBack.dto;


public class AclCheckResponse {
    private boolean allowed;

    // Getters and Setters

    public AclCheckResponse(boolean allowed) {
        this.allowed = allowed;
    }

    public AclCheckResponse() {
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
