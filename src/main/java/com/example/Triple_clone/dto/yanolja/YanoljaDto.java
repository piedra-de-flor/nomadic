package com.example.Triple_clone.dto.yanolja;

public record YanoljaDto(
        String name,
        double score,
        String category,
        int lentTime,
        long lentPrice,
        boolean lentStatus,
        String enterTime,
        long discountRate,
        long originPrice,
        long totalPrice
) {
}
