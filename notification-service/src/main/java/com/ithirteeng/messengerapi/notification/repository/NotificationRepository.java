package com.ithirteeng.messengerapi.notification.repository;

import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Integer countByUserIdAndStatus(UUID userId, NotificationStatus status);

    Boolean existsAllByIdAndUserId(UUID id, UUID userId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status, n.readTime=:time WHERE n.id IN :notifications")
    void changeStatusOfNotifications(List<UUID> notifications, NotificationStatus status, LocalDateTime time);
}
