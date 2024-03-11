package com.example.Triple_clone.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataForYanoljaScraping {
    INITIAL_DATA_FOR_INTEGER(0),
    START_INDEX_TO_EXTRACT_DISCOUNT_DATA(8),
    START_INDEX_TO_EXTRACT_ENTER_TIME(2),
    END_INDEX_TO_EXTRACT_ENTER_TIME(7),
    INDEX_TO_EXTRACT_DATA_FROM_SPLIT(1),
    INDEX_FOR_USELESS_DATA(0),
    MINIMUM_DATA_LENGTH_HAS_DISCOUNT(10),
    MINIMUM_DATA_LENGTH_HAS_DATA(2),
    MINIMUM_DATA_SIZE_HAS_LENT_SERVICE(6),
    INDEX_DATA_ZERO(0);

    private final int value;
}
