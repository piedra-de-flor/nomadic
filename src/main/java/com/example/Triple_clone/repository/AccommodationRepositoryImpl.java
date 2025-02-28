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
public class AccommodationRepositoryImpl implements CustomAccommodationRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Accommodation> findAllByConditions(String local,
                                                   String name,
                                                   String lentDiscountRate,
                                                   String startLentPrice,
                                                   String endLentPrice,
                                                   String category,
                                                   String score,
                                                   String lentStatus,
                                                   String enterTime,
                                                   String lodgmentDiscountRate,
                                                   String startLodgmentPrice,
                                                   String endLodgmentPrice,
                                                   String lodgmentStatus,
                                                   Pageable pageable) {
        JPAQuery<Accommodation> query = jpaQueryFactory
                .selectFrom(accommodation)
                .where(stringEq(QueryDslStringConditions.LOCAL.name(), local),
                        stringIn(QueryDslStringConditions.NAME.name(), name),
                        priceEq(startLentPrice, endLentPrice, QueryDslPriceConditions.LENT.name()),
                        stringIn(QueryDslStringConditions.CATEGORY.name(), category),
                        scoreGoe(score),
                        lentStatusEq(lentStatus),
                        enterTimeGoe(enterTime),
                        //discountRateGoe(lodgmentDiscountRate),
                        priceEq(startLodgmentPrice, endLodgmentPrice, QueryDslPriceConditions.LODGE.name()));

        List<Accommodation> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(accommodation.count())
                .from(accommodation)
                .where(stringEq(QueryDslStringConditions.LOCAL.name(), local),
                        stringIn(QueryDslStringConditions.NAME.name(), name),
                        priceEq(startLentPrice, endLentPrice, QueryDslPriceConditions.LENT.name()),
                        stringIn(QueryDslStringConditions.CATEGORY.name(), category),
                        scoreGoe(score),
                        lentStatusEq(lentStatus),
                        enterTimeGoe(enterTime),
                        //discountRateGoe(lodgmentDiscountRate),
                        priceEq(startLodgmentPrice, endLodgmentPrice, QueryDslPriceConditions.LODGE.name()));

        long total = countQuery.fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    private BooleanExpression stringIn(String conditionType, String condition) {
        if (condition == null) {
            return null;
        }
        QueryDslStringConditions conditions = QueryDslStringConditions.valueOf(conditionType);
        return conditions.getCondition().in(condition);
    }

    private BooleanExpression stringEq(String conditionType, String condition) {
        if (condition == null) {
            return null;
        }
        QueryDslStringConditions conditions = QueryDslStringConditions.valueOf(conditionType);
        return conditions.getCondition().eq(condition);
    }

    private BooleanExpression priceEq(String startPriceCondition, String endPriceCondition, String priceType) {
        Long startPrice = stringToLong(startPriceCondition);
        Long endPrice = stringToLong(endPriceCondition);
        QueryDslPriceConditions prices = QueryDslPriceConditions.valueOf(priceType);

        if (startPriceCondition == null && endPriceCondition == null) {
            return null;
        } else if (startPriceCondition != null && endPriceCondition == null) {
            return prices.getCondition().goe(startPrice);
        } else if (startPriceCondition == null) {
            return prices.getCondition().loe(endPrice);
        } else {
            return prices.getCondition().between(startPrice, endPrice);
        }
    }

    private BooleanExpression scoreGoe(String scoreCondition) {
        if (scoreCondition == null) {
            return null;
        }
        Double score = Double.parseDouble(scoreCondition);
        return accommodation.score.goe(score);
    }

    private BooleanExpression lentStatusEq(String lentStatusCondition) {
        if (lentStatusCondition == null) {
            return null;
        }
        boolean status = LentStatus.valueOf(lentStatusCondition).isStatus();
        return accommodation.lentStatus.eq(status);
    }

    private BooleanExpression enterTimeGoe(String enterTimeCondition) {
        if (enterTimeCondition == null) {
            return null;
        }
        LocalTime time = LocalTime.parse(enterTimeCondition, DateTimeFormatter.ofPattern("HH:mm"));
        return accommodation.enterTime.goe(time);
    }

  /*  private BooleanExpression discountRateGoe(String discountRateCondition) {
        if (discountRateCondition == null) {
            return null;
        }
        Long discountRate = stringToLong(discountRateCondition);
        return accommodation.discountRate.goe(discountRate);
    }*/

    private Long stringToLong(String string) {
        if (string == null) {
            return null;
        }
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("QueryDsl : 문자열을 숫자로 변경하는데 오류가 발생했습니다.");
        }
    }
}
