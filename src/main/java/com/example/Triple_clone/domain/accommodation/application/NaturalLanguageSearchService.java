package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.web.dto.SearchParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaturalLanguageSearchService {

    private final SpellCheckService spellCheckService;

    private static final Map<String, List<String>> LOCATION_KEYWORDS = Map.of(
            "바다뷰", Arrays.asList("바다", "오션뷰", "씨뷰", "해변", "해안", "비치", "바닷가", "해변가", "오션", "sea", "ocean", "beach"),
            "산뷰", Arrays.asList("산", "산뷰", "마운틴뷰", "산악", "등산", "mountain", "산속", "산기슭"),
            "도심", Arrays.asList("도심", "시내", "중심가", "번화가", "downtown", "시티", "도시", "중앙"),
            "강변", Arrays.asList("강", "강변", "리버뷰", "river", "한강", "강가"),
            "공원", Arrays.asList("공원", "park", "숲", "자연", "녹지")
    );

    private static final Map<String, List<String>> PRICE_KEYWORDS = Map.of(
            "30000", Arrays.asList("3만원", "삼만원", "30000", "3만", "30k"),
            "50000", Arrays.asList("5만원", "오만원", "50000", "5만", "50k"),
            "100000", Arrays.asList("10만원", "십만원", "100000", "10만", "100k"),
            "150000", Arrays.asList("15만원", "십오만원", "150000", "15만", "150k"),
            "200000", Arrays.asList("20만원", "이십만원", "200000", "20만", "200k"),
            "300000", Arrays.asList("30만원", "삼십만원", "300000", "30만", "300k")
    );

    private static final Map<String, List<String>> TIME_KEYWORDS = Map.of(
            "late_checkout", Arrays.asList("퇴실이 늦은", "늦은 퇴실", "레이트 체크아웃", "늦게 나가는", "퇴실 늦은", "체크아웃 늦은",
                    "늦은 체크아웃", "퇴실시간 늦은", "체크아웃 연장", "퇴실연장", "late checkout"),
            "early_checkin", Arrays.asList("일찍 들어가는", "얼리 체크인", "early checkin", "이른 체크인", "입실 빠른", "체크인 빠른"),
            "long_stay", Arrays.asList("장기", "긴 숙박", "오래", "연박", "장기 숙박")
    );

    private static final Map<String, List<String>> FACILITY_KEYWORDS = Map.of(
            "pool", Arrays.asList("수영장", "풀", "pool", "워터파크", "물놀이"),
            "spa", Arrays.asList("스파", "사우나", "온천", "찜질방", "spa", "목욕탕"),
            "gym", Arrays.asList("헬스장", "피트니스", "gym", "운동", "운동시설"),
            "parking", Arrays.asList("주차", "주차장", "parking", "주차 가능", "무료주차"),
            "wifi", Arrays.asList("와이파이", "wifi", "인터넷", "무선인터넷", "무료와이파이"),
            "pet", Arrays.asList("반려동물", "펫", "pet", "강아지", "고양이", "애완동물"),
            "breakfast", Arrays.asList("조식", "아침식사", "breakfast", "조식포함", "무료조식"),
            "bbq", Arrays.asList("바베큐", "bbq", "고기구이", "바비큐", "그릴")
    );

    private static final Map<String, List<String>> DISCOUNT_KEYWORDS = Map.of(
            "discount", Arrays.asList("할인", "세일", "저렴한", "싼", "특가", "프로모션", "이벤트", "할인중", "세일중",
                    "discount", "sale", "cheap", "저가", "할인가", "특별가", "기획상품")
    );

    private static final Map<String, List<String>> CATEGORY_KEYWORDS = Map.of(
            "HOTEL", Arrays.asList("호텔", "hotel", "비즈니스호텔", "시티호텔", "부티크호텔"),
            "PENSION", Arrays.asList("펜션", "pension", "별장", "빌라", "독채"),
            "RESORT", Arrays.asList("리조트", "resort", "콘도", "콘도미니엄"),
            "MOTEL", Arrays.asList("모텔", "motel"),
            "GUESTHOUSE", Arrays.asList("게스트하우스", "민박", "guesthouse", "홈스테이")
    );

    private static final Map<String, List<String>> ROOM_TYPE_KEYWORDS = Map.of(
            "ocean_view", Arrays.asList("오션뷰", "바다뷰", "씨뷰", "ocean view", "sea view"),
            "city_view", Arrays.asList("시티뷰", "도심뷰", "city view"),
            "suite", Arrays.asList("스위트", "suite", "스위트룸", "대형객실"),
            "twin", Arrays.asList("트윈", "twin", "트윈베드", "침대 2개"),
            "double", Arrays.asList("더블", "double", "더블베드", "킹베드", "퀸베드"),
            "ondol", Arrays.asList("온돌", "한실", "바닥난방", "한국식")
    );

    private static final Map<String, List<String>> CAPACITY_KEYWORDS = Map.of(
            "2", Arrays.asList("2인", "두명", "커플", "couple", "둘이", "2명"),
            "3", Arrays.asList("3인", "세명", "3명", "셋이"),
            "4", Arrays.asList("4인", "네명", "가족", "family", "4명", "넷이"),
            "6", Arrays.asList("6인", "여섯명", "6명", "대가족", "단체"),
            "8", Arrays.asList("8인", "여덟명", "8명", "대형", "단체숙박")
    );

    public SearchParams parseAdvancedNaturalLanguage(String query) {
        String correctedQuery = spellCheckService.correctSpelling(query.toLowerCase());
        log.info("원본 쿼리: '{}' → 교정된 쿼리: '{}'", query, correctedQuery);

        SearchParams params = SearchParams.builder().build();
        List<String> searchKeywords = new ArrayList<>();

        params.setOriginalQuery(query);
        params.setCorrectedQuery(correctedQuery);

        for (Map.Entry<String, List<String>> entry : LOCATION_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getKey());
                params.addLocationContext(entry.getKey());
            }
        }

        Integer priceMax = extractPriceRange(correctedQuery);
        if (priceMax != null) {
            if (containsAnyKeyword(correctedQuery, Arrays.asList("이하", "아래", "미만", "까지", "대 이하", "원 이하"))) {
                params.setRoomPriceMax(priceMax);
            } else if (containsAnyKeyword(correctedQuery, Arrays.asList("이상", "위", "초과", "부터", "대 이상", "원 이상"))) {
                params.setRoomPriceMin(priceMax);
            } else {
                params.setRoomPriceMin((int)(priceMax * 0.8));
                params.setRoomPriceMax((int)(priceMax * 1.2));
            }
        }

        if (containsAnyKeyword(correctedQuery, TIME_KEYWORDS.get("late_checkout"))) {
            params.setLateCheckout(true);
            searchKeywords.add("레이트체크아웃");
        }

        if (containsAnyKeyword(correctedQuery, TIME_KEYWORDS.get("early_checkin"))) {
            params.setEarlyCheckin(true);
            searchKeywords.add("얼리체크인");
        }

        for (Map.Entry<String, List<String>> entry : FACILITY_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getKey());
                params.addFacility(entry.getKey());
            }
        }

        if (containsAnyKeyword(correctedQuery, DISCOUNT_KEYWORDS.get("discount"))) {
            params.setHasDiscount(true);
            searchKeywords.add("할인");
        }

        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                params.setCategory(entry.getKey());
                break;
            }
        }

        Integer capacity = extractCapacity(correctedQuery);
        if (capacity != null) {
            params.setRoomCapacityMin(capacity);
        }

        for (Map.Entry<String, List<String>> entry : ROOM_TYPE_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(correctedQuery, entry.getValue())) {
                searchKeywords.add(entry.getValue().get(0)); // 첫 번째 키워드 사용
                params.addRoomType(entry.getKey());
            }
        }

        String region = extractRegion(correctedQuery);
        if (region != null) {
            params.setRegion(region);
            searchKeywords.add(region);
        }

        searchKeywords.add(correctedQuery);
        params.setSearchKeyword(String.join(" ", searchKeywords));

        params.setSuggestions(generateSearchSuggestions(correctedQuery));

        log.info("파싱 결과: {}", params);
        return params;
    }

    private Integer extractPriceRange(String query) {
        Pattern pricePattern = Pattern.compile("(\\d+)\\s*(만원?|원|k|천원?)");
        Matcher matcher = pricePattern.matcher(query);

        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "만원", "만" -> { return number * 10000; }
                case "천원", "천" -> { return number * 1000; }
                case "k", "K" -> { return number * 1000; }
                default -> {
                    if (number > 1000) return number;
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : PRICE_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(query, entry.getValue())) {
                return Integer.valueOf(entry.getKey());
            }
        }

        return null;
    }

    private Integer extractCapacity(String query) {
        Pattern capacityPattern = Pattern.compile("(\\d+)\\s*(명|인|사람)");
        Matcher matcher = capacityPattern.matcher(query);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        // 키워드 기반 인원 추출
        for (Map.Entry<String, List<String>> entry : CAPACITY_KEYWORDS.entrySet()) {
            if (containsAnyKeyword(query, entry.getValue())) {
                return Integer.valueOf(entry.getKey());
            }
        }

        return null;
    }

    private String extractRegion(String query) {
        Map<String, List<String>> regionKeywords = Map.of(
                "서울", Arrays.asList("서울", "강남", "강북", "홍대", "명동", "이태원", "압구정", "신촌"),
                "부산", Arrays.asList("부산", "해운대", "광안리", "남포동", "서면"),
                "제주", Arrays.asList("제주", "제주도", "서귀포", "중문"),
                "경기", Arrays.asList("경기", "수원", "성남", "고양", "인천", "분당"),
                "강원", Arrays.asList("강원", "춘천", "강릉", "속초", "평창", "원주"),
                "경남", Arrays.asList("경남", "통영", "거제", "남해", "창원"),
                "전남", Arrays.asList("전남", "여수", "순천", "광주", "목포"),
                "충남", Arrays.asList("충남", "천안", "아산", "보령", "대전"),
                "경북", Arrays.asList("경북", "경주", "안동", "포항", "대구")
        );

        for (Map.Entry<String, List<String>> entry : regionKeywords.entrySet()) {
            if (containsAnyKeyword(query, entry.getValue())) {
                return entry.getKey();
            }
        }

        return null;
    }

    private List<String> generateSearchSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();

        // 유사한 검색어 제안
        if (query.contains("바다")) {
            suggestions.add("바다뷰 오션뷰 해변 숙소");
            suggestions.add("해변가 근처 호텔");
        }

        if (query.contains("할인")) {
            suggestions.add("특가 할인 프로모션 숙소");
            suggestions.add("세일 중인 호텔");
        }

        if (query.contains("가족")) {
            suggestions.add("가족 단위 4인실 숙소");
            suggestions.add("어린이 동반 가능한 호텔");
        }

        if (query.contains("커플")) {
            suggestions.add("커플 전용 펜션");
            suggestions.add("로맨틱 호텔 스위트룸");
        }

        if (query.contains("수영장")) {
            suggestions.add("수영장 있는 리조트");
            suggestions.add("풀빌라 독채");
        }

        return suggestions.stream().limit(5).collect(Collectors.toList());
    }

    private boolean containsAnyKeyword(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }
}
