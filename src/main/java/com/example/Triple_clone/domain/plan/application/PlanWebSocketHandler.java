package com.example.Triple_clone.domain.plan.application;

import com.example.Triple_clone.domain.plan.domain.DetailPlan;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateDto;
import com.example.Triple_clone.domain.plan.web.dto.detailplan.DetailPlanUpdateResultDto;
import com.example.Triple_clone.domain.plan.web.dto.websocket.WebSocketMessage;
import com.example.Triple_clone.domain.plan.web.dto.websocket.EditingStateMessage;
import com.example.Triple_clone.domain.plan.web.dto.websocket.RealTimeChangeMessage;
import com.example.Triple_clone.domain.plan.web.dto.websocket.WebSocketMessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final DetailPlanService detailPlanService;

    private final Map<Long, CopyOnWriteArraySet<WebSocketSession>> planSessions = new ConcurrentHashMap<>();
    private final Map<Long, EditingInfo> editingPlans = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long planId = (Long) session.getAttributes().get("planId");
        String userEmail = (String) session.getAttributes().get("userEmail");
        String userName = (String) session.getAttributes().get("userName");

        log.info("WebSocket 연결 established: user={}, planId={}", userName, planId);

        planSessions.computeIfAbsent(planId, k -> new CopyOnWriteArraySet<>()).add(session);

        WebSocketMessage joinMessage = WebSocketMessage.createUserMessage(
                WebSocketMessageType.PLAN_JOINED,
                Map.of(
                        "currentEditing", getCurrentEditingInfo(planId),
                        "connectedUsers", getConnectedUsers(planId)
                ),
                userEmail,
                userName
        );

        sendToSession(session, joinMessage);

        WebSocketMessage userJoinedMessage = WebSocketMessage.createUserMessage(
                WebSocketMessageType.USER_JOINED,
                Map.of(
                        "userEmail", userEmail,
                        "userName", userName,
                        "connectedUsers", getConnectedUsers(planId)
                ),
                userEmail,
                userName
        );

        broadcastToPlan(planId, userJoinedMessage, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            WebSocketMessage wsMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);

            String userEmail = (String) session.getAttributes().get("userEmail");
            String userName = (String) session.getAttributes().get("userName");
            Long planId = (Long) session.getAttributes().get("planId");
            Boolean hasEditPermission = (Boolean) session.getAttributes().get("hasEditPermission");

            if (userEmail == null || planId == null) {
                sendError(session, "세션 정보를 찾을 수 없습니다.");
                return;
            }

            switch (wsMessage.type()) {
                case START_EDIT:
                    handleStartEdit(session, wsMessage, planId, userEmail, userName, hasEditPermission);
                    break;
                case UPDATE_DETAIL_PLAN:
                    handleUpdateDetailPlan(session, wsMessage, planId, userEmail, userName, hasEditPermission);
                    break;
                case CANCEL_EDIT:
                    handleCancelEdit(session, wsMessage, planId, userEmail, userName);
                    break;
                case REAL_TIME_CHANGE:
                    handleRealTimeChange(session, wsMessage, planId, userEmail, userName);
                    break;
                default:
                    sendError(session, "지원하지 않는 메시지 타입입니다: " + wsMessage.type());
            }
        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 중 오류 발생", e);
            sendError(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }

    private void handleStartEdit(WebSocketSession session, WebSocketMessage message,
                                 Long planId, String userEmail, String userName, Boolean hasEditPermission) throws IOException {
        if (!Boolean.TRUE.equals(hasEditPermission)) {
            sendError(session, "편집 권한이 없습니다.");
            return;
        }

        Long detailPlanId = message.detailPlanId();
        if (detailPlanId == null) {
            sendError(session, "DetailPlan ID가 필요합니다.");
            return;
        }

        EditingInfo existingEdit = editingPlans.get(detailPlanId);
        if (existingEdit != null && !existingEdit.sessionId().equals(session.getId())) {
            sendError(session, "다른 사용자가 편집 중입니다: " + existingEdit.userEmail);
            return;
        }

        try {
            DetailPlan detailPlan = detailPlanService.findById(detailPlanId);

            editingPlans.put(detailPlanId, new EditingInfo(
                    session.getId(),
                    userEmail,
                    userName,
                    System.currentTimeMillis(),
                    detailPlan.getVersion()
            ));

            EditingStateMessage editingState = EditingStateMessage.createStart(detailPlanId, userEmail, userName);
            WebSocketMessage editStartedMessage = WebSocketMessage.createPlanMessage(
                    WebSocketMessageType.EDIT_STARTED, editingState, detailPlanId, userEmail, userName);

            broadcastToPlan(planId, editStartedMessage, null);

        } catch (Exception e) {
            sendError(session, "편집 시작 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void handleUpdateDetailPlan(WebSocketSession session, WebSocketMessage message,
                                        Long planId, String userEmail, String userName, Boolean hasEditPermission) throws IOException {
        if (!Boolean.TRUE.equals(hasEditPermission)) {
            sendError(session, "편집 권한이 없습니다.");
            return;
        }

        Long detailPlanId = message.detailPlanId();
        if (detailPlanId == null) {
            sendError(session, "DetailPlan ID가 필요합니다.");
            return;
        }

        EditingInfo editingInfo = editingPlans.get(detailPlanId);
        if (editingInfo == null || !editingInfo.sessionId().equals(session.getId())) {
            sendError(session, "편집 권한이 없습니다. 먼저 편집을 시작해주세요.");
            return;
        }

        try {
            Map<String, Object> data = (Map<String, Object>) message.data();
            DetailPlanUpdateDto updateDto = objectMapper.convertValue(data, DetailPlanUpdateDto.class);

            DetailPlan currentDetailPlan = detailPlanService.findById(detailPlanId);
            if (!currentDetailPlan.getVersion().equals(editingInfo.originalVersion())) {
                sendError(session, "다른 사용자가 이미 수정했습니다. 새로고침 후 다시 시도해주세요.");
                editingPlans.remove(detailPlanId);
                return;
            }

            DetailPlanUpdateResultDto result = detailPlanService.update(currentDetailPlan, updateDto);

            editingPlans.remove(detailPlanId);

            WebSocketMessage updateMessage = WebSocketMessage.createPlanMessage(
                    WebSocketMessageType.DETAIL_PLAN_UPDATED,
                    Map.of(
                            "detailPlanId", detailPlanId,
                            "updateResult", result,
                            "updatedBy", userName
                    ),
                    detailPlanId,
                    userEmail,
                    userName
            );

            broadcastToPlan(planId, updateMessage, null);

        } catch (Exception e) {
            log.error("DetailPlan 업데이트 중 오류 발생", e);
            editingPlans.remove(detailPlanId);
            sendError(session, "업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void handleCancelEdit(WebSocketSession session, WebSocketMessage message,
                                  Long planId, String userEmail, String userName) throws IOException {
        Long detailPlanId = message.detailPlanId();
        if (detailPlanId == null) {
            sendError(session, "DetailPlan ID가 필요합니다.");
            return;
        }

        EditingInfo editingInfo = editingPlans.get(detailPlanId);
        if (editingInfo != null && editingInfo.sessionId().equals(session.getId())) {
            editingPlans.remove(detailPlanId);

            EditingStateMessage editingState = EditingStateMessage.createStop(detailPlanId, userEmail, userName);
            WebSocketMessage cancelMessage = WebSocketMessage.createPlanMessage(
                    WebSocketMessageType.EDIT_CANCELLED, editingState, detailPlanId, userEmail, userName);

            broadcastToPlan(planId, cancelMessage, null);
        }
    }

    private void handleRealTimeChange(WebSocketSession session, WebSocketMessage message,
                                      Long planId, String userEmail, String userName) throws IOException {
        Map<String, Object> data = (Map<String, Object>) message.data();
        RealTimeChangeMessage changeMessage = objectMapper.convertValue(data, RealTimeChangeMessage.class);

        RealTimeChangeMessage messageWithUser = changeMessage.withUserInfo(userEmail, userName);

        WebSocketMessage realtimeMessage = WebSocketMessage.createPlanMessage(
                WebSocketMessageType.REAL_TIME_CHANGE, messageWithUser, message.detailPlanId(), userEmail, userName);

        broadcastToPlan(planId, realtimeMessage, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long planId = (Long) session.getAttributes().get("planId");
        String userEmail = (String) session.getAttributes().get("userEmail");
        String userName = (String) session.getAttributes().get("userName");

        log.info("WebSocket 연결 종료: user={}, planId={}", userName, planId);

        if (planId != null) {
            CopyOnWriteArraySet<WebSocketSession> sessions = planSessions.get(planId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    planSessions.remove(planId);
                }
            }

            editingPlans.entrySet().removeIf(entry -> {
                if (entry.getValue().sessionId().equals(session.getId())) {
                    try {
                        EditingStateMessage editingState = EditingStateMessage.createStop(entry.getKey(), userEmail, userName);
                        WebSocketMessage cancelMessage = WebSocketMessage.createPlanMessage(
                                WebSocketMessageType.EDIT_CANCELLED, editingState, entry.getKey(), userEmail, userName);
                        broadcastToPlan(planId, cancelMessage, null);
                    } catch (IOException e) {
                        log.error("편집 취소 알림 전송 실패", e);
                    }
                    return true;
                }
                return false;
            });

            try {
                WebSocketMessage userLeftMessage = WebSocketMessage.createUserMessage(
                        WebSocketMessageType.USER_LEFT,
                        Map.of(
                                "userEmail", userEmail,
                                "userName", userName,
                                "connectedUsers", getConnectedUsers(planId)
                        ),
                        userEmail,
                        userName
                );
                broadcastToPlan(planId, userLeftMessage, session);
            } catch (IOException e) {
                log.error("사용자 퇴장 알림 전송 실패", e);
            }
        }
    }

    private void broadcastToPlan(Long planId, WebSocketMessage message, WebSocketSession excludeSession) throws IOException {
        CopyOnWriteArraySet<WebSocketSession> sessions = planSessions.get(planId);
        if (sessions == null) return;

        String messageJson = objectMapper.writeValueAsString(message);

        for (WebSocketSession session : sessions) {
            if (session.equals(excludeSession)) continue;
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (IOException e) {
                    log.error("메시지 전송 실패: {}", session.getId(), e);
                }
            }
        }
    }

    private void sendToSession(WebSocketSession session, WebSocketMessage message) throws IOException {
        String messageJson = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(messageJson));
    }

    private void sendError(WebSocketSession session, String errorMessage) throws IOException {
        String userEmail = (String) session.getAttributes().get("userEmail");
        String userName = (String) session.getAttributes().get("userName");

        WebSocketMessage errorMsg = WebSocketMessage.error(errorMessage, userEmail, userName);
        sendToSession(session, errorMsg);
    }

    private Map<String, Object> getCurrentEditingInfo(Long planId) {
        Map<String, Object> editingInfo = new ConcurrentHashMap<>();

        editingPlans.forEach((detailPlanId, editInfo) -> {
            editingInfo.put(detailPlanId.toString(), Map.of(
                    "userEmail", editInfo.userEmail(),
                    "userName", editInfo.userName(),
                    "startTime", editInfo.startTime()
            ));
        });

        return editingInfo;
    }

    private List<Map<String, String>> getConnectedUsers(Long planId) {
        CopyOnWriteArraySet<WebSocketSession> sessions = planSessions.get(planId);
        if (sessions == null) return Collections.emptyList();

        return sessions.stream()
                .filter(WebSocketSession::isOpen)
                .map(session -> Map.of(
                        "userEmail", (String) session.getAttributes().get("userEmail"),
                        "userName", (String) session.getAttributes().get("userName")
                ))
                .collect(Collectors.toList());
    }

        @Getter
        private record EditingInfo(String sessionId, String userEmail, String userName, long startTime, Long originalVersion) {
    }
}