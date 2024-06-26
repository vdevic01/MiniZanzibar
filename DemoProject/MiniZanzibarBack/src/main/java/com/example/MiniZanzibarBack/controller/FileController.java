package com.example.MiniZanzibarBack.controller;

import com.example.MiniZanzibarBack.model.Document;
import com.example.MiniZanzibarBack.model.User;
import com.example.MiniZanzibarBack.repository.UserRepository;
import com.example.MiniZanzibarBack.service.implementation.DocumentService;
import com.example.MiniZanzibarBack.service.implementation.UserService;
import com.example.MiniZanzibarBack.service.implementation.ZanzibarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private static final String UPLOAD_DIR = new FileSystemResource("").getFile().getAbsolutePath() + "/src/main/resources/files";

    private final UserRepository userRepository;
    private final UserService userService;
    private final DocumentService documentService;
    private final ZanzibarService zanzibarService;

    @Autowired
    public FileController(UserRepository userRepository, UserService userService, DocumentService documentService, ZanzibarService zanzibarService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.documentService = documentService;
        this.zanzibarService = zanzibarService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Invalid file!", HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.BAD_REQUEST);
        }

        Document document = new Document();
        document.setOwner(user.get());
        document.setName(file.getOriginalFilename());
        document = documentService.save(document);

        try {
            // Create the upload directory if it does not exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file to the specified directory
            String filePath = UPLOAD_DIR + File.separator + document.getId();
            file.transferTo(new File(filePath));

            // Create ACL for the user to have access to the file
            zanzibarService.createAcl(user.get().getId().toString(), document.getId().toString(), "owner");

            return new ResponseEntity<>("Document uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Check if the user has permission to access the file
        boolean hasAccess = zanzibarService.checkAccess(user.get().getId().toString(), fileId, "viewer");
        if (!hasAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Load file as Resource
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileId).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        // Check if file exists
        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + fileId);
        }

        Document document = documentService.findById(Long.valueOf(fileId));

        // Determine content type based on file extension
        String contentType = "application/octet-stream"; // Default to binary data if contentType is not detectable

        // Set Content-Disposition header to prompt file download
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(resource);
    }
}

