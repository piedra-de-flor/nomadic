package com.example.Triple_clone.domain.plan.web.websocket;

import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.application.PlanPermissionUtils;
import com.example.Triple_clone.domain.plan.application.PlanService;
import com.example.Triple_clone.domain.plan.domain.Plan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanWebSocketInterceptor implements HandshakeInterceptor {

    private final PlanService planService;
    private final UserService userService;
    private final PlanPermissionUtils planPermissionUtils;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        try {
            String path = request.getURI().getPath();
            Long planId = extractPlanId(path);
            String email = extractEmailFromRequest(request);

            Member member = userService.findByEmail(email);
            Plan plan = planService.findById(planId);

            if (!planPermissionUtils.hasViewPermission(plan, member)) {
                log.warn("WebSocket 연결 권한 없음: email={}, planId={}", email, planId);
                return false;
            }

            attributes.put("planId", planId);
            attributes.put("userId", member.getId());
            attributes.put("userEmail", member.getEmail());
            attributes.put("userName", member.getName());
            attributes.put("hasEditPermission", planPermissionUtils.hasEditPermission(plan, member));

            log.info("WebSocket 연결 성공: user={}, planId={}", member.getName(), planId);
            return true;
        } catch (Exception e) {
            log.error("WebSocket handshake 실패: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private Long extractPlanId(String path) {
        try {
            String[] pathSegments = path.split("/");
            String planIdString = pathSegments[pathSegments.length - 1];
            return Long.parseLong(planIdString);
        } catch (Exception e) {
            log.warn("유효하지 않은 계획 ID: {}", path);
            throw new IllegalArgumentException("웹소켓 연결 실패: " + path, e);
        }
    }

    private String extractEmailFromRequest(ServerHttpRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return null;
    }
}