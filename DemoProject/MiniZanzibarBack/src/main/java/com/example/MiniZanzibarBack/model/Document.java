package com.example.MiniZanzibarBack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "documents")
@Entity
public class Document extends BaseEntityAudit {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User owner;
}
