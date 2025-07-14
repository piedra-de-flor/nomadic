package com.example.Triple_clone.batch;

import com.example.Triple_clone.common.logging.logMessage.BatchLogMessage;
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
        log.info(BatchLogMessage.BATCH_PROCESS_STARTED.format("숙소 정보 크롤링"));
        for (ScrapingLocal local : ScrapingLocal.values()) {
            scrapeYanolja(local.getKoreanName());
        }
    }

    public CompletableFuture<Void> scrapeYanolja(String location) {
        return webClient.get()
                .uri("/scrape/{location}", location)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    log.error(BatchLogMessage.BATCH_PROCESS_FAILED.format(location + " 숙소 정보 크롤링", response.statusCode()));
                    return Mono.error(new RuntimeException("크롤링 실패: " + response.statusCode()));
                })
                .bodyToMono(String.class)
                .doOnSuccess(response ->  log.info(BatchLogMessage.BATCH_PROCESS_ENDED.format(location + " 숙소 정보 크롤링")))
                .doOnError(error -> log.error(BatchLogMessage.BATCH_PROCESS_FAILED.format(location + " 숙소 정보 크롤링", error.getMessage())))
                .then()
                .toFuture();
    }
}
