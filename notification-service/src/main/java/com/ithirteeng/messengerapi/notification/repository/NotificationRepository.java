package com.ithirteeng.messengerapi.notification.repository;

import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Integer countByUserIdAndStatus(UUID userId, NotificationStatus status);
}
