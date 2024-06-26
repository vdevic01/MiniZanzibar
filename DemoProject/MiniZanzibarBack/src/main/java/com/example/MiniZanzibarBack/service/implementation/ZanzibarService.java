package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.dto.AclCheckResponse;
import com.example.MiniZanzibarBack.model.Document;
import com.example.MiniZanzibarBack.repository.BaseJPARepository;
import com.example.MiniZanzibarBack.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ZanzibarService {
    private final RestTemplate restTemplate;

    @Autowired
    public ZanzibarService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public boolean checkAccess(String userId, String fileId, String relation) {
        String url = "http://localhost:8080/api/acl/check";
        Map<String, String> request = Map.of(
                "namespace", "doc",
                "object_id", fileId,
                "relation", relation,
                "user_id", userId
        );

        ResponseEntity<AclCheckResponse> response = restTemplate.postForEntity(url, request, AclCheckResponse.class);
        return response.getBody() != null && response.getBody().isAllowed();
    }

    public void createAcl(String userId, String fileId, String relation) {
        String url = "http://localhost:8080/api/acl";
        Map<String, String> request = Map.of(
                "namespace", "doc",
                "object_id", fileId,
                "relation", relation,
                "user_id", userId
        );

        restTemplate.postForEntity(url, request, Void.class);
    }
}

