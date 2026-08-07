package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.global.error.CustomException;
import com.example.mainpofol.global.error.ErrorCode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
@RequiredArgsConstructor
public class MyInfoDocumentService {

    private static final String MYINFO_PATH = "classpath:static/myinfo/";
    private static final Pattern CHECKLIST_LINK_PATTERN = Pattern.compile("\\]\\(\\./([^)]*\\.md)\\)");

    private final ResourceLoader resourceLoader;

    public String loadChecklist() {
        return loadDocument("checklist.md");
    }

    public List<String> extractDocumentFileNames(String checklist) {
        Matcher matcher = CHECKLIST_LINK_PATTERN.matcher(checklist);
        return matcher.results()
                .map(result -> result.group(1))
                .distinct()
                .toList();
    }

    public String loadDocuments(List<String> fileNames) {
        return fileNames.stream()
                .map(fileName -> """
                        ---
                        file: %s

                        %s
                        """.formatted(fileName, loadDocument(fileName)))
                .reduce("", (left, right) -> left + "\n" + right);
    }

    private String loadDocument(String fileName) {
        Resource resource = resourceLoader.getResource(MYINFO_PATH + fileName);
        if (!resource.exists()) {
            throw new CustomException(ErrorCode.MYINFO_DOCUMENT_NOT_FOUND, "문서를 찾을 수 없습니다: " + fileName);
        }

        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.MYINFO_DOCUMENT_READ_FAILED, "문서를 읽을 수 없습니다: " + fileName, e);
        }
    }
}
