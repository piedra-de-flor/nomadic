package com.example.Triple_clone.domain.accommodation.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum RegionKeywords {

    SEOUL("서울", "서울", "강남", "강북", "홍대", "명동", "이태원", "압구정", "신촌"),
    BUSAN("부산", "부산", "해운대", "광안리", "남포동", "서면"),
    JEJU("제주", "제주", "제주도", "서귀포", "중문"),
    GYEONGGI("경기", "경기", "수원", "성남", "고양", "인천", "분당"),
    GANGWON("강원", "강원", "춘천", "강릉", "속초", "평창", "원주"),
    GYEONGNAM("경남", "경남", "통영", "거제", "남해", "창원"),
    JEONNAM("전남", "전남", "여수", "순천", "광주", "목포"),
    CHUNGNAM("충남", "충남", "천안", "아산", "보령", "대전"),
    GYEONGBUK("경북", "경북", "경주", "안동", "포항", "대구");

    private final String key;
    private final List<String> synonyms;

    RegionKeywords(String key, String... synonyms) {
        this.key = key;
        this.synonyms = Arrays.asList(synonyms);
    }

    public String getKey() {
        return key;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    // 모든 지역 키워드를 Map으로 반환
    public static Map<String, List<String>> getAllRegionKeywords() {
        return Arrays.stream(values())
                .collect(Collectors.toMap(RegionKeywords::getKey, RegionKeywords::getSynonyms));
    }

    // 특정 텍스트에서 지역 추출
    public static String extractRegion(String text) {
        for (RegionKeywords region : values()) {
            if (region.getSynonyms().stream().anyMatch(text::contains)) {
                return region.getKey();
            }
        }
        return null;
    }
}