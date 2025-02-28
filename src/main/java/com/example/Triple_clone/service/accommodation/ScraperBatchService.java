package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.vo.ScrapingLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ScraperBatchService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 1200000)
    public void scrapeAllLocations() {
        log.info("==== 크롤링 시작 ====");
        for (ScrapingLocal location : ScrapingLocal.values()) {
            scrapeYanolja(location.getKoreanName());
        }
    }

    @Async
    public CompletableFuture<Void> scrapeYanolja(String location) {
        String url = "http://localhost:8000/scrape/" + location;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && "success".equals(responseBody.get("status"))) {
                    log.info("✅ 크롤링 성공: {}", location);
                } else {
                    log.error("❌ 크롤링 실패: {} - 이유: {}", location, responseBody.get("error"));
                }
            } else {
                log.error("❌ 크롤링 요청 실패: {} - HTTP 상태 코드: {}", location, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ 크롤링 요청 중 예외 발생: {}", location, e);
        }
        return CompletableFuture.completedFuture(null);
    }
}
