package com.example.Triple_clone.domain.vo;

public enum ScrapingLocal {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SUWON("수원"),
    SEONGNAM("성남"),
    UIJEONGBU("의정부"),
    ANYANG("안양"),
    BUCHEON("부천"),
    ANSAN("안산"),
    GWANGMYEONG("광명"),
    PYEONGTAEK("평택"),
    GIMPO("김포"),
    GOYANG("고양"),
    NAMYANGJU("남양주"),
    YONGIN("용인"),
    ICHEON("이천"),
    ANSEONG("안성"),
    CHUNCHEON("춘천"),
    GANGNEUNG("강릉"),
    WONJU("원주"),
    JEONJU("전주"),
    GWANGYANG("광양"),
    YEOSU("여수"),
    MOKPO("목포"),
    SUNCHEON("순천"),
    CHEONGJU("청주"),
    JINCHEON("진천"),
    SEJONG("세종"),
    ANDONG("안동"),
    POHANG("포항"),
    GYEONGJU("경주"),
    CHANGWON("창원"),
    GIMHAE("김해"),
    JINJU("진주"),
    TONGYEONG("통영"),
    JEJU("제주"),
    SEOGWIPO("서귀포"),
    SOKCHO("속초"),
    DONGHAE("동해"),
    SAMCHEOK("삼척");


    private final String koreanName;

    ScrapingLocal(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}

