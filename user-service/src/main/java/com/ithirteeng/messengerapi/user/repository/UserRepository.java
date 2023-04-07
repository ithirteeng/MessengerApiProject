package com.ithirteeng.messengerapi.user.repository;

import com.ithirteeng.messengerapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для всех данных, связанных с {@link UserEntity}
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Метод для проверки на существование пользователя по логину или email
     *
     * @param login логин
     * @param email емэйл
     * @return {@link Boolean}
     */
    Boolean existsByLoginOrEmail(String login, String email);

    /**
     * Метод для получение данных о пользователе по логину
     *
     * @param login логин
     * @return объект типа {@link Optional<UserEntity>}
     */
    Optional<UserEntity> findByLogin(String login);
}
