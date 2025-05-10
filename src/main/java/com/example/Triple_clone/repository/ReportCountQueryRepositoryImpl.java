package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.QReportCount;
import com.example.Triple_clone.dto.report.ReportCountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportCountQueryRepositoryImpl implements ReportCountQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportCountDto> searchReportCounts(String targetType, Long minReportCount, Pageable pageable) {
        QReportCount reportCount = QReportCount.reportCount;

        JPAQuery<ReportCountDto> query = queryFactory
                .select(Projections.constructor(
                        ReportCountDto.class,
                        reportCount.targetId,
                        reportCount.targetType,
                        reportCount.reportCount
                ))
                .from(reportCount)
                .where(
                        eqTargetType(targetType),
                        goeMinReportCount(minReportCount)
                );

        List<ReportCountDto> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(reportCount.count())
                .from(reportCount)
                .where(
                        eqTargetType(targetType),
                        goeMinReportCount(minReportCount)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression eqTargetType(String targetType) {
        return targetType != null ? QReportCount.reportCount.targetType.eq(targetType) : null;
    }

    private BooleanExpression goeMinReportCount(Long minReportCount) {
        return minReportCount != null ? QReportCount.reportCount.count.goe(minReportCount) : null;
    }
}


