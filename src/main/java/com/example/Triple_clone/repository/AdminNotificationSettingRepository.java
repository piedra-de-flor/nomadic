package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.AdminNotificationSetting;
import com.example.Triple_clone.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AdminNotificationSettingRepository extends JpaRepository<AdminNotificationSetting, Long> {

    Optional<AdminNotificationSetting> findByAdmin(Member admin);

    List<AdminNotificationSetting> findAll();
}
