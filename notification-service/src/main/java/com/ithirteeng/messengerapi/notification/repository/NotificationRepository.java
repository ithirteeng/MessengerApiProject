package com.ithirteeng.messengerapi.notification.repository;

import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с таблицей уведомлений
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID>,
        JpaSpecificationExecutor<NotificationEntity> {

    /**
     * Метод для подсчета уведомлений пользователя по статусу
     *
     * @param userId идентификатор пользователя
     * @param status статус уведомления ({@link NotificationStatus})
     * @return {@link Integer}
     */
    Integer countByUserIdAndStatus(UUID userId, NotificationStatus status);

    /**
     * Методя для проверки на существования уведосления по идентифкатору онного и идентификатору юзера
     *
     * @param id идентификатор сообщения
     * @param userId идентификатор пользователя
     * @return {@link Boolean}
     */
    Boolean existsAllByIdAndUserId(UUID id, UUID userId);

    /**
     * Метод для обоновления статуса сразу у множества уведослений
     *
     * @param notifications список идентификаторов уведомлений
     * @param status статус
     * @param time время прочтения уведомления
     */
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status, n.readTime=:time WHERE n.id IN :notifications")
    void changeStatusOfNotifications(List<UUID> notifications, NotificationStatus status, LocalDateTime time);
}
