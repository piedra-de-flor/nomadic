package com.example.Triple_clone.domain.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StringDataForYanoljaScraping {
    BLANK_LINE("/n"),
    DISCOUNT_DATA_IDENTIFIER("%"),
    PRICE_COMMA(","),
    RESERVATION_SOLD_OUT("예약마감"),
    SCORE_DECIMAL_POINT("."),
    PRICE_UNIT("원"),
    LENT_SERVICE("대실"),
    TIME("시간"),
    INQUIRY("문의"),
    REMOVE(""),
    WHITE_SPACE(" ");

    private final String value;
}
