package com.ithirteeng.messengerapi.notification.utils;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Класс - спецификация для уведомления
 */
public class NotificationSpecification {

    /**
     * Метод, создающий спецификацию, который берет все уведомления, дата получения которых больше полученное
     *
     * @param dateTime дата получения
     * @return {@link Specification}<{@link NotificationEntity}>
     */
    public static Specification<NotificationEntity> greaterReceivedDate(LocalDateTime dateTime) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("receiveTime"), dateTime);
    }

    /**
     * Метод, создающий спецификацию, который берет все уведомления, дата получения которых меньше полученное
     *
     * @param dateTime дата получения
     * @return {@link Specification}<{@link NotificationEntity}>
     */
    public static Specification<NotificationEntity> lessReceivedDate(LocalDateTime dateTime) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("receiveTime"), dateTime);
    }

    /**
     * Метод, создающий спецификацию, который берет все уведомления, текст которых подходит под данный
     *
     * @param text текст запроса
     * @return {@link Specification}<{@link NotificationEntity}>
     */
    public static Specification<NotificationEntity> textLike(String text) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("text")), "%" + text.toLowerCase() + "%");
    }

    /**
     * Метод, создающий спецификацию, который берет все уведомления, тип которых находится в списке данных типов
     *
     * @param types список типов уведомлений
     * @return {@link Specification}<{@link NotificationEntity}>
     */
    public static Specification<NotificationEntity> typeIn(List<NotificationType> types) {
        return (root, query, builder) -> root.get("type").in(types);
    }

    /**
     * Метод, создающий спецификацию, который берет все уведомления, идентификатор юзера которых равен данному
     *
     * @param userId идентификатор пользователя
     * Метод создающий запрос, который берет все уведомления, текст которых подходит под данный
     */
    public static Specification<NotificationEntity> userIdEqual(UUID userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userId"), userId);
    }

}
