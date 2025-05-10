package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.QMember;
import com.example.Triple_clone.domain.entity.QReport;
import com.example.Triple_clone.dto.report.QReportResponseDto;
import com.example.Triple_clone.dto.report.ReportSearchDto;
import com.example.Triple_clone.dto.report.ReportResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportAdminRepositoryImpl implements ReportAdminRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReportResponseDto> searchReports(ReportSearchDto condition, Pageable pageable) {
        QReport report = QReport.report;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getTargetType() != null) {
            builder.and(report.targetType.eq(condition.getTargetType()));
        }
        if (condition.getStatus() != null) {
            builder.and(report.status.eq(condition.getStatus()));
        }
        if (condition.getReason() != null) {
            builder.and(report.reason.eq(condition.getReason()));
        }
        if (condition.getFromDate() != null) {
            builder.and(report.createdAt.goe(condition.getFromDate()));
        }
        if (condition.getToDate() != null) {
            builder.and(report.createdAt.loe(condition.getToDate()));
        }

        List<ReportResponseDto> results = queryFactory
                .select(new QReportResponseDto(
                        report.id,
                        report.targetType,
                        report.targetId,
                        member.name,
                        report.reason,
                        report.status,
                        report.createdAt
                ))
                .from(report)
                .join(report.reporter, member)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(report.createdAt.desc())
                .fetch();

        long total = queryFactory
                .select(report.count())
                .from(report)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
