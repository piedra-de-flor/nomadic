package com.example.Triple_clone.domain.plan.web.websocket;

import com.example.Triple_clone.common.auth.JwtTokenProvider;
import com.example.Triple_clone.domain.member.application.UserService;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.plan.application.PlanPermissionUtils;
import com.example.Triple_clone.domain.plan.application.PlanService;
import com.example.Triple_clone.domain.plan.application.PlanShareService;
import com.example.Triple_clone.domain.plan.domain.Plan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanWebSocketInterceptor implements HandshakeInterceptor {

    private final PlanService planService;
    private final UserService userService;
    private final PlanShareService planShareService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        try {
            String token = resolveToken(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                log.warn("WebSocket 연결 실패: 유효하지 않은 JWT 토큰");
                return false;
            }

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            String email = authentication.getName();

            String path = request.getURI().getPath();
            Long planId = extractPlanId(path);

            Member member = userService.findByEmail(email);
            Plan plan = planService.findById(planId);

            if (!PlanPermissionUtils.hasViewPermission(plan, member, planShareService)) {
                log.warn("WebSocket 연결 권한 없음: email={}, planId={}", email, planId);
                return false;
            }

            attributes.put("planId", planId);
            attributes.put("userId", member.getId());
            attributes.put("userEmail", member.getEmail());
            attributes.put("userName", member.getName());
            attributes.put("hasEditPermission", PlanPermissionUtils.hasEditPermission(plan, member, planShareService));

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
        if (exception != null) {
            log.error("WebSocket handshake 후 오류 발생", exception);
        }
    }

    private Long extractPlanId(String path) {
        try {
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 3) {
                throw new IllegalArgumentException("잘못된 WebSocket 경로: " + path);
            }

            String planIdString = pathSegments[pathSegments.length - 1];
            return Long.parseLong(planIdString);
        } catch (NumberFormatException e) {
            log.warn("유효하지 않은 계획 ID: {}", path);
            throw new IllegalArgumentException("웹소켓 연결 실패: 잘못된 계획 ID", e);
        }
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}