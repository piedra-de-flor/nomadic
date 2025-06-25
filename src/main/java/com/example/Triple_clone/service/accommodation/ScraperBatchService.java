package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.vo.LogMessage;
import com.example.Triple_clone.domain.vo.ScrapingLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@ConditionalOnProperty(name = "scraper.enabled", havingValue = "true", matchIfMissing = true)
public class ScraperBatchService {
    private final WebClient webClient = WebClient.create("http://fastapi-container:8000");

    @Scheduled(fixedRate = 6000000)
    public void scrapeAllLocations() {
        log.info(LogMessage.BATCH_PROCESS_START.format("숙소 크롤링"));
        for (ScrapingLocal local : ScrapingLocal.values()) {
            scrapeYanolja(local.getKoreanName());
        }
    }

    public CompletableFuture<Void> scrapeYanolja(String location) {
        return webClient.get()
                .uri("/scrape/{location}", location)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    log.error(LogMessage.BATCH_PROCESS_FAIL.format(location + " 숙소 크롤링", response.statusCode()));
                    return Mono.error(new RuntimeException("크롤링 실패: " + response.statusCode()));
                })
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info(LogMessage.BATCH_PROCESS_SUCCESS.format(location + " 숙소 크롤링")))
                .doOnError(error -> log.error(LogMessage.BATCH_PROCESS_FAIL.format(location + " 숙소 크롤링", error)))
                .then()
                .toFuture();
    }
}
