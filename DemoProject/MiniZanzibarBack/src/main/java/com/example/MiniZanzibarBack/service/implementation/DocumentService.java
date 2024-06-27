package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.dto.DocumentDTO;
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
    public void delete(Document document) {
        documentRepository.delete(document);
    }
    public DocumentDTO convertToDTO(Document document) {
        return new DocumentDTO(document.getId(), document.getName(), document.getOwner().getEmail(), document.getCreatedAt(), document.getUpdatedAt());
    }
}
