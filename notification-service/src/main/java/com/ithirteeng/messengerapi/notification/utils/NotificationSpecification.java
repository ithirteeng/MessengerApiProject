package com.ithirteeng.messengerapi.notification.utils;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NotificationSpecification {

    public static Specification<NotificationEntity> greaterReceivedDate(LocalDateTime dateTime) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("receiveTime"), dateTime);
    }

    public static Specification<NotificationEntity> lessReceivedDate(LocalDateTime dateTime) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("receiveTime"), dateTime);
    }

    public static Specification<NotificationEntity> textLike(String text) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("text")), "%" + text.toLowerCase() + "%");
    }

    public static Specification<NotificationEntity> typeIn(List<NotificationType> types) {
        return (root, query, builder) -> root.get("type").in(types);
    }

    public static Specification<NotificationEntity> userIdEqual(UUID userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

}
