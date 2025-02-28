package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.vo.ScrapingLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ScraperBatchService {
    private final WebClient webClient = WebClient.create("http://localhost:8000");

    @Scheduled(fixedRate = 6000000)
    public void scrapeAllLocations() {
        log.info("==== 크롤링 시작 ====");
        for (ScrapingLocal local : ScrapingLocal.values()) {
            scrapeYanolja(local.getKoreanName());
        }
    }

    public CompletableFuture<Void> scrapeYanolja(String location) {
        return webClient.get()
                .uri("/scrape/{location}", location)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response -> {
                    log.error("❌ 크롤링 요청 실패: {} - HTTP 상태 코드: {}", location, response.statusCode());
                    return Mono.error(new RuntimeException("크롤링 실패: " + response.statusCode()));
                })
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("✅ 크롤링 성공: {}", location))
                .doOnError(error -> log.error("❌ 크롤링 중 예외 발생: {}", location, error))
                .then()
                .toFuture();
    }
}
