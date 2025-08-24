package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.common.error.JsonProcessingErrorCode;
import com.example.Triple_clone.common.error.RestApiException;
import com.example.Triple_clone.common.logging.logMessage.JsonProcessingLogMessage;
import com.example.Triple_clone.domain.plan.domain.PlanChangeHistory;
import com.example.Triple_clone.domain.plan.web.dto.plan.event.PlanChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanChangeHistoryEventListener {
    private final PlanChangeHistoryService historyService;
    private final ObjectMapper objectMapper;

    @Async("planHistoryExecutor")
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePlanChangeEvent(PlanChangeEvent event) {
        try {
            String changeData = objectMapper.writeValueAsString(event.getChangeData());

            PlanChangeHistory history = PlanChangeHistory.builder()
                    .plan(event.getPlan())
                    .changedBy(event.getChangedBy())
                    .changeType(event.getChangeType())
                    .targetId(event.getTargetId())
                    .targetType(event.getTargetType().getValue())
                    .changeData(changeData)
                    .build();

            historyService.save(history);

        } catch (JsonProcessingException e) {
            log.error(JsonProcessingLogMessage.JSON_PROCESSING_LOG_MESSAGE.format(
                    event.getTargetType().getValue(), event.getChangeType().getDescription()));
            throw new RestApiException(JsonProcessingErrorCode.JSON_PROCESSING_ERROR_CODE);
        }
    }
}