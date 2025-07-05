package com.example.Triple_clone.domain.notification.infra;

import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.notification.domain.AdminNotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AdminNotificationSettingRepository extends JpaRepository<AdminNotificationSetting, Long> {

    Optional<AdminNotificationSetting> findByAdmin(Member admin);

    List<AdminNotificationSetting> findAll();
}
