package com.ithirteeng.messengerapi.friends.repository;

import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для сервиса друзей
 */
@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {

    /**
     * Метод для получения объекта {@link Optional}<{@link FriendEntity}> из БД
     *
     * @param targetUserId Id целевого пользователя
     * @param addingUserId Id внешнего пользователя
     * @return {@link Optional}<{@link FriendEntity}>
     */
    Optional<FriendEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    /**
     * Метод для получения {@link List}<{@link FriendEntity}> из БД по wildcard фильтру fullName
     *
     * @param fullName     wildcard фильтр
     * @param targetUserId Id целевого пользователя
     * @return {@link List}<{@link FriendEntity}>
     */
    @Query("SELECT f FROM FriendEntity f WHERE f.fullName LIKE %:fullName% AND f.deleteFriendDate = null AND f.targetUserId = :targetUserId")
    List<FriendEntity> findByFullNameLikeAndTargetUserId(@Param("fullName") String fullName, @Param("targetUserId") UUID targetUserId);

    /**
     * Метод для получения {@link Page}<{@link FriendEntity}> из БД
     *
     * @param targetUserId Id целевого пользователя
     * @param pageable     объект класса {@link Pageable}
     * @return {@link Page}<{@link FriendEntity}>
     */
    Page<FriendEntity> findAllByTargetUserId(UUID targetUserId, Pageable pageable);

    /**
     * Метод для проверки существования записи в БД
     *
     * @param targetUserId Id целевого пользователя
     * @param addingUserId Id внешнего пользователя
     * @return {@link Boolean}
     */
    Boolean existsByAddingUserIdAndTargetUserId(UUID addingUserId, UUID targetUserId);

    /**
     * Метод для обновления поля fullName в БД
     *
     * @param addingUserId Id внешнего пользователя
     * @param fullName     имя внешнего пользователя
     */
    @Modifying
    @Query("UPDATE FriendEntity f SET f.fullName = :fullName WHERE f.addingUserId = :addingUserId")
    void updateFullNameByAddingUserId(@Param("addingUserId") UUID addingUserId, @Param("fullName") String fullName);
}
