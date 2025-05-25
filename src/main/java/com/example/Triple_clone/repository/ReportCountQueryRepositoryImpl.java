package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.QReport;
import com.example.Triple_clone.domain.vo.ReportStatus;
import com.example.Triple_clone.domain.vo.ReportTargetType;
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
    public Page<ReportCountDto> searchReportCounts(ReportTargetType targetType, Long minReportCount, Pageable pageable) {
        QReport report = QReport.report;

        JPAQuery<ReportCountDto> query = queryFactory
                .select(Projections.constructor(
                        ReportCountDto.class,
                        report.targetId,
                        report.targetType,
                        report.count()
                ))
                .from(report)
                .where(
                        report.status.eq(ReportStatus.APPROVED),
                        eqTargetType(targetType)
                )
                .groupBy(report.targetId, report.targetType)
                .having(report.count().goe(minReportCount))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ReportCountDto> content = query.fetch();

        Long total = queryFactory
                .select(report.targetId.countDistinct())
                .from(report)
                .where(
                        report.status.eq(ReportStatus.APPROVED),
                        eqTargetType(targetType)
                )
                .groupBy(report.targetId, report.targetType)
                .having(report.count().goe(minReportCount))
                .fetch()
                .size() * 1L;

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression eqTargetType(ReportTargetType targetType) {
        return targetType != null ? QReport.report.targetType.eq(targetType) : null;
    }

}