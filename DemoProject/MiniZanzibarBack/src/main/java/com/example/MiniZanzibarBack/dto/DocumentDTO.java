package com.example.MiniZanzibarBack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Long id;
    private String name;
    private String ownerEmail;
    private Date createdAt;
    private Date updatedAt;
}
