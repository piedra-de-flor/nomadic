package com.example.Triple_clone.domain.notification;

import com.example.Triple_clone.domain.notification.notification.NotificationSearchDto;
import com.example.Triple_clone.domain.notification.notification.NotificationService;
import com.example.Triple_clone.common.MemberEmailAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public List<NotificationSearchDto> getNotifications(@MemberEmailAspect String email) {
        return notificationService.getUserNotifications(email);
    }

    @PostMapping("notifications/read/{memberId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long memberId,
                                           @MemberEmailAspect String email) {
        notificationService.markAsRead(email, memberId);
        return ResponseEntity.ok().build();
    }
}