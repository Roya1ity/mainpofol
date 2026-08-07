package com.example.mainpofol.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final RestClient.Builder restClientBuilder;

    @Value("${telegram.bot-token:}")
    private String botToken;

    @Value("${telegram.chat-id:}")
    private String chatId;

    public void sendQuestionAndAnswer(String question, String answer) {
        if (!StringUtils.hasText(botToken) || !StringUtils.hasText(chatId)) {
            log.warn("Telegram message skipped because TELEGRAM_BOT_TOKEN or TELEGRAM_CHAT_ID is empty.");
            return;
        }

        String text = """
                질문:
                %s

                답변:
                %s
                """.formatted(question, answer);

        try {
            restClientBuilder.build()
                    .post()
                    .uri("https://api.telegram.org/bot{token}/sendMessage", botToken)
                    .body(new TelegramSendMessageRequest(chatId, text))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException e) {
            log.error("Failed to send Telegram message.", e);
        }
    }

    private record TelegramSendMessageRequest(String chat_id, String text) {
    }
}
