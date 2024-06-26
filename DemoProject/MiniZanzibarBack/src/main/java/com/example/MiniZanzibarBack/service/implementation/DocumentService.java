package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.model.Document;
import com.example.MiniZanzibarBack.repository.BaseJPARepository;
import com.example.MiniZanzibarBack.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService extends BaseService<Document>{
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    protected BaseJPARepository<Document> getRepository() {
        return documentRepository;
    }
}
