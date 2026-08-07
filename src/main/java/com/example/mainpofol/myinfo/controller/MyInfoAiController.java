package com.example.mainpofol.myinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mainpofol.myinfo.dto.MyInfoAskRequest;
import com.example.mainpofol.myinfo.dto.MyInfoAskResponse;
import com.example.mainpofol.myinfo.service.MyInfoAiService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ask")
public class MyInfoAiController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final MyInfoAiService myInfoAiService;

    @PostMapping
    public CompletableFuture<MyInfoAskResponse> ask(@Valid @RequestBody MyInfoAskRequest request) {
        return myInfoAiService.askAsync(request.getQuestion());
    }

    @PostMapping(value = "/progress", produces = "application/x-ndjson")
    public StreamingResponseBody askWithProgress(@Valid @RequestBody MyInfoAskRequest request) {
        return outputStream -> {
            MyInfoAskResponse response = myInfoAiService.askAsync(request.getQuestion(), message -> {
                try {
                    writeEvent(outputStream, Map.of("type", "progress", "message", message));
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to write progress event.", e);
                }
            }).join();
            writeEvent(outputStream, Map.of("type", "done", "data", response));
        };
    }

    private void writeEvent(OutputStream outputStream, Map<String, Object> event) throws IOException {
        outputStream.write(OBJECT_MAPPER.writeValueAsString(event).getBytes(StandardCharsets.UTF_8));
        outputStream.write('\n');
        outputStream.flush();
    }
}
