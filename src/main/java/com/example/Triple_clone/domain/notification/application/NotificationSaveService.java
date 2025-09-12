package com.example.Triple_clone.domain.notification.application;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.notification.domain.Notification;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.infra.NotificationRepository;
import com.example.Triple_clone.domain.notification.infra.NotificationStatusQueue;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSaveRequest;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSaveService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final NotificationStatusQueue queue;

    public void save(NotificationSaveRequest request) {
        if (request.target() == NotificationTarget.GLOBAL) {
            List<String> userRole = List.of("USER");
            List<Member> allMembers = memberRepository.findAllByRolesIn(userRole);

            Notification notification = Notification.builder()
                    .type(request.type())
                    .target(request.target())
                    .title(request.title())
                    .content(request.content())
                    .targetUserId(null)
                    .build();
            Notification savedNotification = notificationRepository.save(notification);

            for (Member member : allMembers) {
                queue.enqueue(new NotificationSentEvent(savedNotification.getId(), member.getId()));
            }
        } else {
            for (Long memberId : request.targetUserIds()) {
                Notification notification = Notification.builder()
                        .type(request.type())
                        .target(request.target())
                        .title(request.title())
                        .content(request.content())
                        .targetUserId(memberId)
                        .build();
                Notification savedNotification = notificationRepository.save(notification);

                queue.enqueue(new NotificationSentEvent(savedNotification.getId(), memberId));
            }
        }
    }
}
