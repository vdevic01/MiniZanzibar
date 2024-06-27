package com.example.MiniZanzibarBack.controller;

import com.example.MiniZanzibarBack.dto.DocumentDTO;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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



    @GetMapping("/owned-files")
    public ResponseEntity<List<DocumentDTO>> getOwnedFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get owned file IDs from Zanzibar service
        List<String> ownedFileIds = zanzibarService.getOwnedFiles(user.get().getId().toString());

        // Fetch documents from the database and convert to DTOs
        List<DocumentDTO> ownedDocuments = ownedFileIds.stream()
                .map(id -> {
                    Document document = documentService.findById(Long.valueOf(id));
                    return document != null ? documentService.convertToDTO(document) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ResponseEntity<>(ownedDocuments, HttpStatus.OK);
    }

    @GetMapping("/accessible-files")
    public ResponseEntity<List<DocumentDTO>> getAccessibleFiles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Get accessible file IDs from Zanzibar service
        List<String> accessibleFileIds = zanzibarService.getAccessibleFiles(user.get().getId().toString());

        // Fetch documents from the database and convert to DTOs
        List<DocumentDTO> accessibleDocuments = accessibleFileIds.stream()
                .map(id -> {
                    Document document = documentService.findById(Long.valueOf(id));
                    return document != null ? documentService.convertToDTO(document) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ResponseEntity<>(accessibleDocuments, HttpStatus.OK);
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

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getName());
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        // Set Content-Disposition header to prompt file download
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/permissions/{fileId:.+}")
    public ResponseEntity<String> addPermission(@PathVariable String fileId, @RequestParam("email") String email, @RequestParam("relation") String relation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.FORBIDDEN);
        }

        Optional<User> userToshareWith = userRepository.findByEmail(email);
        if (userToshareWith.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.FORBIDDEN);
        }

        // Check if the current user is the owner of the file
        Document document = documentService.findById(Long.valueOf(fileId));
        if (document == null || !zanzibarService.checkAccess(user.get().getId().toString(), fileId, "owner")) {
            return new ResponseEntity<>("You do not have permission to grant access to this file!", HttpStatus.FORBIDDEN);
        }

        // Create ACL for the specified user to have access to the file
        zanzibarService.createAcl(userToshareWith.get().getId().toString(), fileId, relation);

        return new ResponseEntity<>("Permission granted successfully.", HttpStatus.OK);
    }


    @DeleteMapping("/permissions/{fileId:.+}")
    public ResponseEntity<String> deletePermission(@PathVariable String fileId, @RequestParam("email") String email, @RequestParam("relation") String relation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.FORBIDDEN);
        }


        Optional<User> userToUnshareWith = userRepository.findByEmail(email);
        if (userToUnshareWith.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.FORBIDDEN);
        }

        // Check if the current user is the owner of the file
        Document document = documentService.findById(Long.valueOf(fileId));
        if (document == null || !zanzibarService.checkAccess(user.get().getId().toString(), fileId, relation)) {
            return new ResponseEntity<>("You do not have permission to delete access to this file!", HttpStatus.FORBIDDEN);
        }

        // Delete ACL for the specified user to remove access to the file
        zanzibarService.deleteAcl(userToUnshareWith.get().getId().toString(), fileId, relation);

        return new ResponseEntity<>("Permission deleted successfully.", HttpStatus.OK);
    }

    @PutMapping("/edit/{fileId:.+}")
    public ResponseEntity<String> editFile(@PathVariable String fileId, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "name", required = false) String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.FORBIDDEN);
        }

        // Check if the user has permission to edit the file
        boolean hasAccess = zanzibarService.checkAccess(user.get().getId().toString(), fileId, "editor");
        if (!hasAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Document document = documentService.findById(Long.valueOf(fileId));
        if (document == null) {
            return new ResponseEntity<>("Document not found!", HttpStatus.NOT_FOUND);
        }

        try {
            if (file != null && !file.isEmpty()) {
                // Save the new file to the specified directory
                String filePath = UPLOAD_DIR + File.separator + document.getId();
                file.transferTo(new File(filePath));
            }

            if (name != null && !name.isEmpty()) {
                // Update the document name
                document.setName(name);
                documentService.save(document);
            }

            return new ResponseEntity<>("Document updated successfully.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{fileId:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByEmail(currentPrincipalName);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Check if the user has permission to delete the file
        boolean hasAccess = zanzibarService.checkAccess(user.get().getId().toString(), fileId, "owner");
        if (!hasAccess) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            // Find the document
            Document document = documentService.findById(Long.valueOf(fileId));
            if (document == null) {
                return new ResponseEntity<>("Document not found!", HttpStatus.NOT_FOUND);
            }

            // Delete the file from the file system
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileId).normalize();
            Files.deleteIfExists(filePath);

            // Delete the document from the database
            documentService.delete(document);

            // Delete ACL for the file
            zanzibarService.deleteAcl(user.get().getId().toString(), fileId, "editor");

            return new ResponseEntity<>("Document deleted successfully.", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

