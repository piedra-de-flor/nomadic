package com.example.Triple_clone.service.notification.channel;

import com.example.Triple_clone.dto.notification.NotificationMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SlackClient {
    @Value("${slack.webhook.url}")
    private String webhookUrl;

    public void sendMessage(NotificationMessage message) {
        WebClient.create(webhookUrl)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("text", formatMessage(message)))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private String formatMessage(NotificationMessage message) {
        return String.format("*%s*\n%s", message.subject(), message.content());
    }
}

