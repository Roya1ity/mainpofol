package com.example.mainpofol.admin.service;

import com.example.mainpofol.admin.dto.MyInfoDocumentCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoDocumentResponse;
import com.example.mainpofol.admin.dto.MyInfoDocumentUpdateRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class AdminMyInfoDocumentService {

    private static final String CHECKLIST_FILE_NAME = "checklist.md";
    private static final Path MYINFO_DIRECTORY = Paths.get("src/main/resources/static/myinfo")
            .toAbsolutePath()
            .normalize();

    public List<MyInfoDocumentResponse> findAll() {
        try {
            ensureDirectory();
            return Files.list(MYINFO_DIRECTORY)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".md"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .map(this::toSummaryResponse)
                    .toList();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read myinfo documents.", e);
        }
    }

    public MyInfoDocumentResponse findByFileName(String fileName) {
        Path path = resolveDocumentPath(fileName);
        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found.");
        }

        try {
            return toResponse(path, Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read document.", e);
        }
    }

    public MyInfoDocumentResponse create(MyInfoDocumentCreateRequest request) {
        Path path = resolveDocumentPath(request.fileName());
        if (Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Document already exists.");
        }

        try {
            ensureDirectory();
            Files.writeString(path, request.content(), StandardCharsets.UTF_8);
            log.info("[AdminMyInfoDocumentService] myinfo document created. fileName={}", request.fileName());
            return toResponse(path, request.content());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create document.", e);
        }
    }

    public MyInfoDocumentResponse update(String fileName, MyInfoDocumentUpdateRequest request) {
        Path path = resolveDocumentPath(fileName);
        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found.");
        }

        try {
            Files.writeString(path, request.content(), StandardCharsets.UTF_8);
            log.info("[AdminMyInfoDocumentService] myinfo document updated. fileName={}", fileName);
            return toResponse(path, request.content());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update document.", e);
        }
    }

    public void delete(String fileName) {
        if (CHECKLIST_FILE_NAME.equals(fileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checklist.md cannot be deleted.");
        }

        Path path = resolveDocumentPath(fileName);
        if (!Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found.");
        }

        try {
            Files.delete(path);
            log.info("[AdminMyInfoDocumentService] myinfo document deleted. fileName={}", fileName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete document.", e);
        }
    }

    private MyInfoDocumentResponse toSummaryResponse(Path path) {
        return toResponse(path, null);
    }

    private MyInfoDocumentResponse toResponse(Path path, String content) {
        try {
            return new MyInfoDocumentResponse(
                    path.getFileName().toString(),
                    Files.size(path),
                    LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault()),
                    content
            );
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read document metadata.", e);
        }
    }

    private Path resolveDocumentPath(String fileName) {
        if (fileName == null
                || fileName.isBlank()
                || !fileName.endsWith(".md")
                || fileName.contains("/")
                || fileName.contains("\\")
                || fileName.contains("..")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid markdown file name.");
        }

        Path path = MYINFO_DIRECTORY.resolve(fileName).normalize();
        if (!path.startsWith(MYINFO_DIRECTORY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid markdown file path.");
        }
        return path;
    }

    private void ensureDirectory() throws IOException {
        Files.createDirectories(MYINFO_DIRECTORY);
    }
}
