package com.example.Triple_clone.domain.accommodation.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum SearchKeywords {

    // 위치 키워드
    OCEAN_VIEW("바다뷰", "바다", "오션뷰", "씨뷰", "해변", "해안", "비치", "바닷가", "해변가", "오션", "sea", "ocean", "beach"),
    MOUNTAIN_VIEW("산뷰", "산", "산뷰", "마운틴뷰", "산악", "등산", "mountain", "산속", "산기슭"),
    DOWNTOWN("도심", "도심", "시내", "중심가", "번화가", "downtown", "시티", "도시", "중앙"),
    RIVERSIDE("강변", "강", "강변", "리버뷰", "river", "한강", "강가"),
    PARK("공원", "공원", "park", "숲", "자연", "녹지"),

    // 가격 키워드
    PRICE_30K("30000", "3만원", "삼만원", "30000", "3만", "30k"),
    PRICE_50K("50000", "5만원", "오만원", "50000", "5만", "50k"),
    PRICE_100K("100000", "10만원", "십만원", "100000", "10만", "100k"),
    PRICE_150K("150000", "15만원", "십오만원", "150000", "15만", "150k"),
    PRICE_200K("200000", "20만원", "이십만원", "200000", "20만", "200k"),
    PRICE_300K("300000", "30만원", "삼십만원", "300000", "30만", "300k"),

    // 시간 키워드
    LATE_CHECKOUT("late_checkout", "퇴실이 늦은", "늦은 퇴실", "레이트 체크아웃", "늦게 나가는", "퇴실 늦은",
            "체크아웃 늦은", "늦은 체크아웃", "퇴실시간 늦은", "체크아웃 연장", "퇴실연장", "late checkout"),
    EARLY_CHECKIN("early_checkin", "일찍 들어가는", "얼리 체크인", "early checkin", "이른 체크인", "입실 빠른", "체크인 빠른"),
    LONG_STAY("long_stay", "장기", "긴 숙박", "오래", "연박", "장기 숙박"),

    // 시설 키워드
    POOL("pool", "수영장", "풀", "pool", "워터파크", "물놀이"),
    SPA("spa", "스파", "사우나", "온천", "찜질방", "spa", "목욕탕"),
    GYM("gym", "헬스장", "피트니스", "gym", "운동", "운동시설"),
    PARKING("parking", "주차", "주차장", "parking", "주차 가능", "무료주차"),
    WIFI("wifi", "와이파이", "wifi", "인터넷", "무선인터넷", "무료와이파이"),
    PET("pet", "반려동물", "펫", "pet", "강아지", "고양이", "애완동물"),
    BREAKFAST("breakfast", "조식", "아침식사", "breakfast", "조식포함", "무료조식"),
    BBQ("bbq", "바베큐", "bbq", "고기구이", "바비큐", "그릴"),

    // 할인 키워드
    DISCOUNT("discount", "할인", "세일", "저렴한", "싼", "특가", "프로모션", "이벤트", "할인중", "세일중",
            "discount", "sale", "cheap", "저가", "할인가", "특별가", "기획상품"),

    // 숙소 카테고리
    HOTEL("HOTEL", "호텔", "hotel", "비즈니스호텔", "시티호텔", "부티크호텔"),
    PENSION("PENSION", "펜션", "pension", "별장", "빌라", "독채"),
    RESORT("RESORT", "리조트", "resort", "콘도", "콘도미니엄"),
    MOTEL("MOTEL", "모텔", "motel"),
    GUESTHOUSE("GUESTHOUSE", "게스트하우스", "민박", "guesthouse", "홈스테이"),

    // 객실 타입
    ROOM_OCEAN_VIEW("ocean_view", "오션뷰", "바다뷰", "씨뷰", "ocean view", "sea view"),
    ROOM_CITY_VIEW("city_view", "시티뷰", "도심뷰", "city view"),
    SUITE("suite", "스위트", "suite", "스위트룸", "대형객실"),
    TWIN("twin", "트윈", "twin", "트윈베드", "침대 2개"),
    DOUBLE("double", "더블", "double", "더블베드", "킹베드", "퀸베드"),
    ONDOL("ondol", "온돌", "한실", "바닥난방", "한국식"),

    // 수용인원
    CAPACITY_2("2", "2인", "두명", "커플", "couple", "둘이", "2명"),
    CAPACITY_3("3", "3인", "세명", "3명", "셋이"),
    CAPACITY_4("4", "4인", "네명", "가족", "family", "4명", "넷이"),
    CAPACITY_6("6", "6인", "여섯명", "6명", "대가족", "단체"),
    CAPACITY_8("8", "8인", "여덟명", "8명", "대형", "단체숙박");

    private final String key;
    private final List<String> synonyms;

    SearchKeywords(String key, String... synonyms) {
        this.key = key;
        this.synonyms = Arrays.asList(synonyms);
    }

    public String getKey() {
        return key;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    // 카테고리별 키워드 조회
    public static Map<String, List<String>> getLocationKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword.name().contains("VIEW") || keyword == DOWNTOWN || keyword == RIVERSIDE || keyword == PARK)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getPriceKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword.name().startsWith("PRICE_"))
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getTimeKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword == LATE_CHECKOUT || keyword == EARLY_CHECKIN || keyword == LONG_STAY)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getFacilityKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword == POOL || keyword == SPA || keyword == GYM ||
                        keyword == PARKING || keyword == WIFI || keyword == PET ||
                        keyword == BREAKFAST || keyword == BBQ)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getDiscountKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword == DISCOUNT)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getCategoryKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword == HOTEL || keyword == PENSION || keyword == RESORT ||
                        keyword == MOTEL || keyword == GUESTHOUSE)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getRoomTypeKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword.name().startsWith("ROOM_") || keyword == SUITE ||
                        keyword == TWIN || keyword == DOUBLE || keyword == ONDOL)
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }

    public static Map<String, List<String>> getCapacityKeywords() {
        return Arrays.stream(values())
                .filter(keyword -> keyword.name().startsWith("CAPACITY_"))
                .collect(Collectors.toMap(SearchKeywords::getKey, SearchKeywords::getSynonyms));
    }
}