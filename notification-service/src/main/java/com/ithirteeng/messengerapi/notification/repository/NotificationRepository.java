package com.ithirteeng.messengerapi.notification.repository;

import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
}
