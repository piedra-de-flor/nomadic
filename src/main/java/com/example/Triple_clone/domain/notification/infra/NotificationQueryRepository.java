package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.domain.notification.domain.QNotification;
import com.example.Triple_clone.domain.notification.domain.QNotificationStatus;
import com.example.Triple_clone.domain.notification.domain.NotificationTarget;
import com.example.Triple_clone.domain.notification.web.dto.NotificationSearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QNotification notification = QNotification.notification;
    private final QNotificationStatus status = QNotificationStatus.notificationStatus;

    public List<NotificationSearchDto> findAllByUserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(
                        NotificationSearchDto.class,
                        notification.id,
                        notification.title,
                        notification.content,
                        notification.sentAt,
                        status.id.isNotNull()
                ))
                .from(notification)
                .leftJoin(status)
                .on(status.notification.id.eq(notification.id)
                        .and(status.userId.eq(userId)))
                .where(
                        notification.target.eq(NotificationTarget.GLOBAL)
                                .or(notification.targetUserId.eq(userId))
                )
                .orderBy(notification.sentAt.desc())
                .fetch();
    }
}
