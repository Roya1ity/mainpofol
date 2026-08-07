package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.global.error.CustomException;
import com.example.mainpofol.global.error.ErrorCode;
import com.example.mainpofol.myinfo.dto.MyInfoAskResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyInfoAiService {

    private final ChatClient.Builder chatClientBuilder;
    private final MyInfoDocumentService documentService;
    private final MyInfoSelectionParser selectionParser;
    private final MyInfoAskPostProcessService postProcessService;

    @Async("myInfoAiExecutor")
    public CompletableFuture<MyInfoAskResponse> askAsync(String question) {
        return CompletableFuture.completedFuture(ask(question));
    }

    @Async("myInfoAiExecutor")
    public CompletableFuture<MyInfoAskResponse> askAsync(String question, Consumer<String> progressConsumer) {
        return CompletableFuture.completedFuture(ask(question, progressConsumer));
    }

    public MyInfoAskResponse ask(String question) {
        return ask(question, ignored -> {
        });
    }

    public MyInfoAskResponse ask(String question, Consumer<String> progressConsumer) {
        long totalStartedAt = System.currentTimeMillis();
        log.info("[MyInfoAiService] ask started. questionLength={}", question == null ? 0 : question.length());

        ChatClient chatClient = chatClientBuilder.build();

        long checklistStartedAt = System.currentTimeMillis();
        String checklist = documentService.loadChecklist();
        List<String> allowedFiles = documentService.extractDocumentFileNames(checklist);
        log.info("[MyInfoAiService] checklist loaded. allowedFileCount={}, checklistLength={}, elapsedMs={}",
                allowedFiles.size(), lengthOf(checklist), elapsedFrom(checklistStartedAt));

        progressConsumer.accept("질문에 맞는 문서리스트 선별중..");
        long tableOfContentsStartedAt = System.currentTimeMillis();
        String tableOfContentsResponse = callOpenAi(() -> chatClient.prompt()
                .system(buildTableOfContentsSystemPrompt())
                .user(buildTableOfContentsUserPrompt(question, checklist))
                .call()
                .content());
        log.info("[MyInfoAiService] table of contents OpenAI call completed. responseLength={}, elapsedMs={}",
                lengthOf(tableOfContentsResponse), elapsedFrom(tableOfContentsStartedAt));

        long documentStartedAt = System.currentTimeMillis();
        List<String> selectedFiles = selectionParser.parseSelectedFiles(tableOfContentsResponse, allowedFiles);
        String selectedDocuments = documentService.loadDocuments(selectedFiles);
        log.info("[MyInfoAiService] selected documents loaded. selectedFileCount={}, selectedFiles={}, documentLength={}, elapsedMs={}",
                selectedFiles.size(), selectedFiles, lengthOf(selectedDocuments), elapsedFrom(documentStartedAt));

        progressConsumer.accept("선별된 문서를 기반으로 답변 생성중..");
        long answerStartedAt = System.currentTimeMillis();
        String answer = callOpenAi(() -> chatClient.prompt()
                .system(buildAnswerSystemPrompt())
                .user(buildAnswerUserPrompt(question, tableOfContentsResponse, selectedDocuments))
                .call()
                .content());
        log.info("[MyInfoAiService] answer OpenAI call completed. answerLength={}, elapsedMs={}",
                lengthOf(answer), elapsedFrom(answerStartedAt));

        postProcessService.saveAndSendTelegram(question, answer, selectedFiles);
        log.info("[MyInfoAiService] async post process requested.");

        log.info("[MyInfoAiService] ask completed. totalElapsedMs={}", elapsedFrom(totalStartedAt));

        return MyInfoAskResponse.builder()
                .question(question)
                .selectedFiles(selectedFiles)
                .tableOfContentsResponse(tableOfContentsResponse)
                .answer(answer)
                .build();
    }

    private String callOpenAi(OpenAiCall openAiCall) {
        try {
            return openAiCall.call();
        } catch (CustomException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.OPENAI_REQUEST_FAILED, ErrorCode.OPENAI_REQUEST_FAILED.getMessage(), e);
        }
    }

    private long elapsedFrom(long startedAt) {
        return System.currentTimeMillis() - startedAt;
    }

    private int lengthOf(String value) {
        return value == null ? 0 : value.length();
    }

    @FunctionalInterface
    private interface OpenAiCall {

        String call();
    }

    private String buildTableOfContentsSystemPrompt() {
        return """
                너는 전남준의 대변인이자 사용자의 질문에 답하기 위해 확인해야 할 자기소개/기술 문서를 고르는 라우터다.
                사용자가 보낸 checklist.md에는 목차, 문서 파일명, 각 문서 설명이 들어 있다.
                checklist.md만 근거로 질문과 관련된 Markdown 파일명을 고른다.

                반드시 아래 JSON 형식 하나만 출력한다.
                {"files":["파일명.md"],"reason":"선정 이유"}

                규칙:
                - files 값에는 checklist.md에 링크로 존재하는 파일명만 넣는다.
                - 질문과 관련 없는 문서는 제외한다.
                - 확신이 낮으면 요약 문서보다 구체 문서를 우선한다.
                - 설명 문장, 코드블록, Markdown을 추가하지 않는다.
                """;
    }

    private String buildTableOfContentsUserPrompt(String question, String checklist) {
        return """
                질문:
                %s

                checklist.md:
                %s
                """.formatted(question, checklist);
    }

    private String buildAnswerSystemPrompt() {
        return """
                너는 전남준의 대변인이자 포트폴리오 질의응답 API다.
                제공된 Markdown 문서 내용만 근거로 답변한다.
                문서에 없는 내용은 추측하지 말고 확인할 수 없다고 말한다.
                답변은 한국어로 작성한다.
                사용자에게 전남준의 추가 정보를 요구하는 식의 표현은 하지 않는다.
                답변 내용에 무엇을 근거로 판단했는지 작성하지 않는다.
                """;
    }

    private String buildAnswerUserPrompt(String question, String tableOfContentsResponse, String selectedDocuments) {
        return """
                질문:
                %s

                1차 목차 선택 결과:
                %s

                선택된 문서 내용:
                %s

                위 문서 내용만 바탕으로 질문에 답변해라.
                """.formatted(question, tableOfContentsResponse, selectedDocuments);
    }
}
