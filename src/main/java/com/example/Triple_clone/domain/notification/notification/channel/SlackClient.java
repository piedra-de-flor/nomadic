package com.example.Triple_clone.domain.notification.notification.channel;

import com.example.Triple_clone.domain.notification.notification.NotificationMessage;
import com.example.Triple_clone.common.SlackMessageTemplateLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class SlackClient {
    @Value("${slack.webhook.url}")
    private String webhookUrl;
    private final SlackMessageTemplateLoader templateLoader;

    public void sendMessage(NotificationMessage message) {
        WebClient.create(webhookUrl)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text", templateLoader.build(message)))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

