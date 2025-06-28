package com.example.Triple_clone.domain.notification;

import com.example.Triple_clone.domain.notification.AdminNotificationSetting;
import com.example.Triple_clone.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AdminNotificationSettingRepository extends JpaRepository<AdminNotificationSetting, Long> {

    Optional<AdminNotificationSetting> findByAdmin(Member admin);

    List<AdminNotificationSetting> findAll();
}
