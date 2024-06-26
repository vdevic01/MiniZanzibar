package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.dto.AccessibleFilesResponse;
import com.example.MiniZanzibarBack.dto.AclCheckResponse;
import com.example.MiniZanzibarBack.model.Document;
import com.example.MiniZanzibarBack.repository.BaseJPARepository;
import com.example.MiniZanzibarBack.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List<String> getOwnedFiles(String userId) {
        String url = String.format("http://localhost:8080/api/acl?namespace=doc&userId=%s", userId);

        ResponseEntity<AccessibleFilesResponse> response = restTemplate.getForEntity(url, AccessibleFilesResponse.class);
        if (response.getBody() != null && response.getBody().getObjects() != null) {
            return response.getBody().getObjects().stream()
                    .filter(object -> "owner".equals(object.getRelation()))
                    .map(AccessibleFilesResponse.AccessibleObject::getObject_id)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<String> getAccessibleFiles(String userId) {
        String url = String.format("http://localhost:8080/api/acl?namespace=doc&userId=%s", userId);

        ResponseEntity<AccessibleFilesResponse> response = restTemplate.getForEntity(url, AccessibleFilesResponse.class);
        if (response.getBody() != null && response.getBody().getObjects() != null) {
            return response.getBody().getObjects().stream()
                    .filter(object -> !"owner".equals(object.getRelation()))
                    .map(AccessibleFilesResponse.AccessibleObject::getObject_id)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
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


    public void deleteAcl(String userId, String fileId, String relation) {
        String url = "http://localhost:8080/api/acl";
        Map<String, String> request = Map.of(
                "namespace", "doc",
                "object_id", fileId,
                "relation", relation,
                "user_id", userId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

}

