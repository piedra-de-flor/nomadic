package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.JsonProcessingErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.JsonProcessingLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.domain.*;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanChangeData;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateChangeData;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanCreateChangeData;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanPartnerChangeData;
import com.example.Triple_clone.domain.plan.web.dto.plan.PlanStyleChangeData;
import com.example.Triple_clone.domain.plan.web.dto.planshare.PlanShareChangeData;
import com.example.Triple_clone.domain.plan.web.dto.planshare.ShareStatusChangeData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanChangeHistoryUtils {
    private final PlanChangeHistoryService historyService;
    private final ObjectMapper objectMapper;

    public void recordDetailPlanCreated(Plan plan, Member member, DetailPlan detailPlan) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    DetailPlanChangeData.builder()
                            .location(detailPlan.getLocation())
                            .date(detailPlan.getDate())
                            .time(detailPlan.getTime())
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.CREATED)
                    .targetId(detailPlan.getId())
                    .targetType(TargetType.DETAIL_PLAN.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.DETAIL_PLAN.getValue(), ChangeType.CREATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordDetailPlanUpdated(Plan plan, Member member, DetailPlan detailPlan,
                                        DetailPlan oldDetailPlan) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    DetailPlanUpdateChangeData.builder()
                            .oldLocation(oldDetailPlan.getLocation())
                            .newLocation(detailPlan.getLocation())
                            .oldDate(oldDetailPlan.getDate())
                            .newDate(detailPlan.getDate())
                            .oldTime(oldDetailPlan.getTime())
                            .newTime(detailPlan.getTime())
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.UPDATED)
                    .targetId(detailPlan.getId())
                    .targetType(TargetType.DETAIL_PLAN.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.DETAIL_PLAN.getValue(), ChangeType.UPDATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordDetailPlanDeleted(Plan plan, Member member, DetailPlan detailPlan) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    DetailPlanChangeData.builder()
                            .location(detailPlan.getLocation())
                            .date(detailPlan.getDate())
                            .time(detailPlan.getTime())
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.DELETED)
                    .targetId(detailPlan.getId())
                    .targetType(TargetType.DETAIL_PLAN.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.DETAIL_PLAN.getValue(), ChangeType.DELETED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanShared(Plan plan, Member sharer, Member sharedMember, ShareRole role) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanShareChangeData.builder()
                            .sharedMemberEmail(sharedMember.getEmail())
                            .sharedMemberName(sharedMember.getName())
                            .role(role)
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(sharer)
                    .changeType(ChangeType.SHARED)
                    .targetId(sharedMember.getId())
                    .targetType(TargetType.MEMBER.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.MEMBER.getValue(), ChangeType.SHARED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanUnShared(Plan plan, Member sharer, Member sharedMember, ShareRole role) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanShareChangeData.builder()
                            .sharedMemberEmail(sharedMember.getEmail())
                            .sharedMemberName(sharedMember.getName())
                            .role(role)
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(sharer)
                    .changeType(ChangeType.UNSHARED)
                    .targetId(sharedMember.getId())
                    .targetType(TargetType.MEMBER.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.MEMBER.getValue(), ChangeType.UNSHARED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanStyleUpdated(Plan plan, Member member, java.util.List<String> oldStyles, java.util.List<String> newStyles) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanStyleChangeData.builder()
                            .oldStyles(oldStyles)
                            .newStyles(newStyles)
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.UPDATED)
                    .targetId(plan.getId())
                    .targetType(TargetType.PLAN_STYLE.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN_STYLE.getValue(), ChangeType.UPDATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanPartnerUpdated(Plan plan, Member member, String oldPartner, String newPartner) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanPartnerChangeData.builder()
                            .oldPartner(oldPartner)
                            .newPartner(newPartner)
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.UPDATED)
                    .targetId(plan.getId())
                    .targetType(TargetType.PLAN_PARTNER.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN_PARTNER.getValue(), ChangeType.UPDATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanCreated(Plan plan, Member member) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanCreateChangeData.builder()
                            .place(plan.getPlace())
                            .partner(plan.getPartner() != null ? plan.getPartner().name() : null)
                            .styles(plan.getStyles().stream().map(Enum::name).collect(java.util.stream.Collectors.toList()))
                            .startDay(plan.getStartDay())
                            .endDay(plan.getEndDay())
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.CREATED)
                    .targetId(plan.getId())
                    .targetType(TargetType.PLAN.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN.getValue(), ChangeType.CREATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordPlanDeleted(Plan plan, Member member) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    PlanCreateChangeData.builder()
                            .place(plan.getPlace())
                            .partner(plan.getPartner() != null ? plan.getPartner().name() : null)
                            .styles(plan.getStyles().stream().map(Enum::name).collect(java.util.stream.Collectors.toList()))
                            .startDay(plan.getStartDay())
                            .endDay(plan.getEndDay())
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.DELETED)
                    .targetId(plan.getId())
                    .targetType(TargetType.PLAN.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN.getValue(), ChangeType.DELETED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordShareAccepted(Plan plan, Member member, PlanShare planShare) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    ShareStatusChangeData.builder()
                            .sharedMemberEmail(member.getEmail())
                            .sharedMemberName(member.getName())
                            .role(planShare.getRole())
                            .oldStatus("PENDING")
                            .newStatus("ACCEPTED")
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.UPDATED)
                    .targetId(planShare.getId())
                    .targetType(TargetType.PLAN_SHARE.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN_SHARE.getValue(), ChangeType.UPDATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }

    public void recordShareRejected(Plan plan, Member member, PlanShare planShare) {
        try {
            String changeData = objectMapper.writeValueAsString(
                    ShareStatusChangeData.builder()
                            .sharedMemberEmail(member.getEmail())
                            .sharedMemberName(member.getName())
                            .role(planShare.getRole())
                            .oldStatus("PENDING")
                            .newStatus("REJECTED")
                            .build()
            );

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(plan)
                    .changedBy(member)
                    .changeType(ChangeType.UPDATED)
                    .targetId(planShare.getId())
                    .targetType(TargetType.PLAN_SHARE.getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);
        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    TargetType.PLAN_SHARE.getValue(), ChangeType.UPDATED.getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }
}