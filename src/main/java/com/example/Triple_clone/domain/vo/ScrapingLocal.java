package com.example.Triple_clone.domain.vo;

public enum ScrapingLocal {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    INCHEON("인천"),
    SUWON("수원");

    private final String koreanName;

    ScrapingLocal(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}

