package com.example.mainpofol.myinfo.service;

import com.example.mainpofol.admin.service.AdminMyInfoAskHistoryService;
import com.example.mainpofol.telegram.TelegramMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyInfoAskPostProcessService {

    private final AdminMyInfoAskHistoryService askHistoryService;
    private final TelegramMessageService telegramMessageService;

    @Async("myInfoPostProcessExecutor")
    public void saveAndSendTelegram(String question, String answer, List<String> selectedFiles) {
        long totalStartedAt = System.currentTimeMillis();
        log.info("[MyInfoAskPostProcessService] async post process started. selectedFileCount={}",
                selectedFiles == null ? 0 : selectedFiles.size());

        try {
            long saveStartedAt = System.currentTimeMillis();
            askHistoryService.save(question, answer, selectedFiles);
            log.info("[MyInfoAskPostProcessService] ask history saved. elapsedMs={}", elapsedFrom(saveStartedAt));
        } catch (RuntimeException e) {
            log.error("[MyInfoAskPostProcessService] ask history save failed.", e);
        }

        try {
            long telegramStartedAt = System.currentTimeMillis();
            telegramMessageService.sendQuestionAndAnswer(question, answer);
            log.info("[MyInfoAskPostProcessService] telegram send step completed. elapsedMs={}",
                    elapsedFrom(telegramStartedAt));
        } catch (RuntimeException e) {
            log.error("[MyInfoAskPostProcessService] telegram send failed.", e);
        }

        log.info("[MyInfoAskPostProcessService] async post process completed. totalElapsedMs={}",
                elapsedFrom(totalStartedAt));
    }

    private long elapsedFrom(long startedAt) {
        return System.currentTimeMillis() - startedAt;
    }
}
