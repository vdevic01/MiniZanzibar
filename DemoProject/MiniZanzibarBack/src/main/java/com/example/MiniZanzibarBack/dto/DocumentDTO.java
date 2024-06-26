package com.example.MiniZanzibarBack.dto;

public class DocumentDTO {
    private Long id;
    private String name;
    private String ownerEmail;

    public DocumentDTO(Long id, String name, String ownerEmail) {
        this.id = id;
        this.name = name;
        this.ownerEmail = ownerEmail;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
