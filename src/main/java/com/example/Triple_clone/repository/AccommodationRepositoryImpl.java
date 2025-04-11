package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.Accommodation;
import com.example.Triple_clone.domain.vo.LentStatus;
import com.example.Triple_clone.domain.vo.QueryDslPriceConditions;
import com.example.Triple_clone.domain.vo.QueryDslStringConditions;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.Triple_clone.domain.entity.QAccommodation.accommodation;

@RequiredArgsConstructor
@Repository
public class AccommodationRepositoryImpl implements CustomAccommodationRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Accommodation> searchByConditionsFromDB(
            String local,
            String name,
            String discountRate,
            String startLentPrice,
            String endLentPrice,
            String category,
            String score,
            String lentStatus,
            String enterTime,
            String startLodgmentPrice,
            String endLodgmentPrice,
            String lodgmentStatus,
            Pageable pageable) {

        BooleanExpression predicate = buildPredicate(
                local, name, discountRate, startLentPrice, endLentPrice, category, score,
                lentStatus, enterTime, startLodgmentPrice, endLodgmentPrice, lodgmentStatus);

        JPAQuery<Accommodation> query = jpaQueryFactory
                .selectFrom(accommodation)
                .where(predicate);

        List<Accommodation> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(accommodation.count())
                .from(accommodation)
                .where(predicate);

        long total = countQuery.fetchCount();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private BooleanExpression buildPredicate(
            String local, String name, String discountRate, String startLentPrice, String endLentPrice,
            String category, String score, String lentStatus, String enterTime,
            String startLodgmentPrice, String endLodgmentPrice, String lodgmentStatus) {

        return stringEq(QueryDslStringConditions.LOCAL.name(), local)
                .and(stringIn(QueryDslStringConditions.NAME.name(), name))
                .and(priceEq(startLentPrice, endLentPrice, QueryDslPriceConditions.LENT.name()))
                .and(stringIn(QueryDslStringConditions.CATEGORY.name(), category))
                .and(scoreGoe(score))
                .and(lentStatusEq(lentStatus))
                .and(enterTimeGoe(enterTime))
                .and(priceEq(startLodgmentPrice, endLodgmentPrice, QueryDslPriceConditions.LODGE.name()))
                .and(discountRateGoe(discountRate)); // 할인율 필터 추가
    }

    private BooleanExpression discountRateGoe(String discountRateCondition) {
        if (discountRateCondition == null || discountRateCondition.isEmpty()) {
            return null;
        }
        try {
            Long discountRate = Long.parseLong(discountRateCondition);
            return accommodation.lodgmentDiscountRate.goe(discountRate)
                    .or(accommodation.lentDiscountRate.goe(discountRate)); // OR 조건으로 둘 중 하나 이상이면 검색
        } catch (NumberFormatException e) {
            return null; // 숫자 변환 실패 시 필터 적용 안 함
        }
    }

    private BooleanExpression stringIn(String conditionType, String condition) {
        if (condition == null || condition.isEmpty()) {
            return null;
        }
        QueryDslStringConditions conditions = QueryDslStringConditions.valueOf(conditionType);
        return conditions.getCondition().in(condition);
    }

    private BooleanExpression stringEq(String conditionType, String condition) {
        if (condition == null || condition.isEmpty()) {
            return null;
        }
        QueryDslStringConditions conditions = QueryDslStringConditions.valueOf(conditionType);
        return conditions.getCondition().eq(condition);
    }

    private BooleanExpression priceEq(String startPriceCondition, String endPriceCondition, String priceType) {
        Long startPrice = stringToLong(startPriceCondition);
        Long endPrice = stringToLong(endPriceCondition);
        QueryDslPriceConditions prices = QueryDslPriceConditions.valueOf(priceType);

        if (startPrice == null && endPrice == null) {
            return null;
        } else if (startPrice != null && endPrice == null) {
            return prices.getCondition().goe(startPrice);
        } else if (startPrice == null) {
            return prices.getCondition().loe(endPrice);
        } else {
            return prices.getCondition().between(startPrice, endPrice);
        }
    }

    private BooleanExpression scoreGoe(String scoreCondition) {
        if (scoreCondition == null || scoreCondition.isEmpty()) {
            return null;
        }
        try {
            Double score = Double.parseDouble(scoreCondition);
            return accommodation.score.goe(score);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BooleanExpression lentStatusEq(String lentStatusCondition) {
        if (lentStatusCondition == null || lentStatusCondition.isEmpty()) {
            return null;
        }
        try {
            boolean status = LentStatus.valueOf(lentStatusCondition).isStatus();
            return accommodation.lentStatus.eq(status);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private BooleanExpression enterTimeGoe(String enterTimeCondition) {
        if (enterTimeCondition == null || enterTimeCondition.isEmpty()) {
            return null;
        }
        try {
            LocalTime time = LocalTime.parse(enterTimeCondition, DateTimeFormatter.ofPattern("HH:mm"));
            return accommodation.enterTime.goe(time);
        } catch (Exception e) {
            return null;
        }
    }

    private Long stringToLong(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
