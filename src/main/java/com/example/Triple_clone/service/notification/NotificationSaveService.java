package com.example.Triple_clone.service.notification;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Notification;
import com.example.Triple_clone.domain.vo.NotificationTarget;
import com.example.Triple_clone.dto.notification.NotificationSaveRequest;
import com.example.Triple_clone.dto.notification.NotificationSentEvent;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.repository.NotificationRepository;
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
            List<Member> allMembers = memberRepository.findAllByRolesEquals(userRole);

            Notification notification = Notification.builder()
                    .type(request.type())
                    .target(request.target())
                    .title(request.title())
                    .content(request.content())
                    .targetUserId(null)
                    .build();
            notificationRepository.save(notification);

            for (Member member : allMembers) {
                queue.enqueue(new NotificationSentEvent(notification.getId(), member.getId()));
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
                notificationRepository.save(notification);

                queue.enqueue(new NotificationSentEvent(notification.getId(), memberId));
            }
        }
    }
}
