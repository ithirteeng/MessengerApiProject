package com.ithirteeng.messengerapi.friends.repository;

import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для черного списка
 */
@Repository
public interface BlackListRepository extends JpaRepository<BlockedUserEntity, UUID> {
    /**
     * Метод для получения объекта {@link Optional}<{@link BlockedUserEntity}> из БД
     *
     * @param targetUserId Id целевого пользователя
     * @param addingUserId Id внешнего пользователя
     * @return {@link Optional}<{@link BlockedUserEntity}>
     */
    Optional<BlockedUserEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    /**
     * Метод для получения {@link List}<{@link BlockedUserEntity}> из БД по wildcard фильтру fullName
     *
     * @param fullName     wildcard фильтр
     * @param targetUserId Id целевого пользователя
     * @return {@link List}<{@link BlockedUserEntity}>
     */
    @Query("SELECT b FROM BlockedUserEntity b WHERE b.fullName LIKE %:fullName% AND b.deleteNoteDate = null AND b.targetUserId = :targetUserId")
    List<BlockedUserEntity> findByFullNameLikeAndTargetUserId(@Param("fullName") String fullName, @Param("targetUserId") UUID targetUserId);

    /**
     * Метод для получения {@link Page}<{@link BlockedUserEntity}> из БД
     *
     * @param targetUserId Id целевого пользователя
     * @param pageable     объект класса {@link Pageable}
     * @return {@link Page}<{@link BlockedUserEntity}>
     */
    Page<BlockedUserEntity> findAllByTargetUserId(UUID targetUserId, Pageable pageable);

    /**
     * Метод для проверки существования записи в БД
     *
     * @param targetUserId Id целевого пользователя
     * @param addingUserId Id внешнего пользователя
     * @return {@link Boolean}
     */
    Boolean existsByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    /**
     * Метод для обновления поля fullName в БД
     *
     * @param addingUserId Id внешнего пользователя
     * @param fullName     имя внешнего пользователя
     */
    @Modifying
    @Query("UPDATE BlockedUserEntity b SET b.fullName = :fullName WHERE b.addingUserId = :addingUserId")
    void updateFullNameByAddingUserId(@Param("addingUserId") UUID addingUserId, @Param("fullName") String fullName);

    /**
     * Метод для проверки нахождения внешнего пользователя в черном списке целевого
     *
     * @param targetUserId   Id целевого пользователя
     * @param addingUserId   Id внешнего пользователя
     * @param deleteNoteDate Дата удаления из черного списка
     * @return {@link Boolean}
     */
    Boolean existsByTargetUserIdAndAddingUserIdAndDeleteNoteDate(UUID targetUserId, UUID addingUserId, Date deleteNoteDate);
}
