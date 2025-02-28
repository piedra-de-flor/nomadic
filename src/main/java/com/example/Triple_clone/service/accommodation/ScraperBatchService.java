package com.example.Triple_clone.service.accommodation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class ScraperBatchService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 100000) // 5분마다 실행
    public void scrapeYanolja() {
        String location = "서울";
        String url = "http://localhost:8000/scrape/" + location;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && "success".equals(responseBody.get("status"))) {
                    log.info("크롤링 성공: " + location);
                } else {
                    log.error("크롤링 실패: " + location + " - 이유: " + responseBody.get("error"));
                }
            } else {
                log.error("크롤링 요청 실패: " + location + " - HTTP 상태 코드: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("크롤링 요청 중 예외 발생: " + location, e);
        }
    }
}
