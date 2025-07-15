package com.example.Triple_clone.domain.notification.web.controller;

import com.example.Triple_clone.common.auth.MemberEmailAspect;
import com.example.Triple_clone.domain.notification.application.NotificationService;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 조회", description = "현재 로그인되어 있는 회원의 알림을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @GetMapping("/notifications")
    public List<NotificationSearchDto> getNotifications(@MemberEmailAspect String email) {
        return notificationService.getUserNotifications(email);
    }

    @Operation(summary = "알림 읽기", description = "특정 알림을 읽음 표시 합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @ApiResponse(responseCode = "401", description = "권한 인증 오류 발생")
    @PostMapping("notifications/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId,
                                           @MemberEmailAspect String email) {
        notificationService.markAsRead(email, notificationId);
        return ResponseEntity.ok().build();
    }
}