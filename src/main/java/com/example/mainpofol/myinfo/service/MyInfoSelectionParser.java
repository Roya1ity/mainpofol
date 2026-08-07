package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.global.error.CustomException;
import com.example.mainpofol.global.error.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MyInfoSelectionParser {

    private static final String PREFIX = "확인해야할 목차리스트";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> parseSelectedFiles(String response, List<String> allowedFiles) {
        Set<String> selectedFiles = parseFiles(response, allowedFiles);
        if (selectedFiles.isEmpty()) {
            selectedFiles.add("종합_개발_역량.md");
            selectedFiles.add("개발자_포지션_요약.md");
        }
        return new ArrayList<>(selectedFiles);
    }

    private Set<String> parseFiles(String response, List<String> allowedFiles) {
        Set<String> allowedFileSet = new LinkedHashSet<>(allowedFiles);
        Set<String> selectedFiles = new LinkedHashSet<>();

        try {
            JsonNode root = objectMapper.readTree(extractJson(response));
            JsonNode files = root.path("files");
            if (files.isArray()) {
                files.forEach(file -> {
                    String fileName = file.asText();
                    if (allowedFileSet.contains(fileName)) {
                        selectedFiles.add(fileName);
                    }
                });
            }
        } catch (Exception ignored) {
            allowedFileSet.stream()
                    .filter(response::contains)
                    .forEach(selectedFiles::add);
        }

        return selectedFiles;
    }

    private String extractJson(String response) {
        int prefixIndex = response.indexOf(PREFIX);
        String target = prefixIndex >= 0 ? response.substring(prefixIndex) : response;
        int start = target.indexOf('{');
        int end = target.lastIndexOf('}');

        if (start < 0 || end < start) {
            throw new CustomException(ErrorCode.OPENAI_REQUEST_FAILED, "목차 선택 응답에서 JSON 객체를 찾을 수 없습니다.");
        }

        return target.substring(start, end + 1);
    }
}
