package com.example.mainpofol.admin.controller;

import com.example.mainpofol.admin.dto.MyInfoDocumentCreateRequest;
import com.example.mainpofol.admin.dto.MyInfoDocumentResponse;
import com.example.mainpofol.admin.dto.MyInfoDocumentUpdateRequest;
import com.example.mainpofol.admin.service.AdminMyInfoDocumentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/myinfo-documents")
public class AdminMyInfoDocumentController {

    private final AdminMyInfoDocumentService documentService;

    @GetMapping
    public List<MyInfoDocumentResponse> findAll() {
        return documentService.findAll();
    }

    @GetMapping("/{fileName}")
    public MyInfoDocumentResponse findByFileName(@PathVariable String fileName) {
        return documentService.findByFileName(fileName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MyInfoDocumentResponse create(@Valid @RequestBody MyInfoDocumentCreateRequest request) {
        return documentService.create(request);
    }

    @PutMapping("/{fileName}")
    public MyInfoDocumentResponse update(
            @PathVariable String fileName,
            @Valid @RequestBody MyInfoDocumentUpdateRequest request
    ) {
        return documentService.update(fileName, request);
    }

    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String fileName) {
        documentService.delete(fileName);
    }
}
