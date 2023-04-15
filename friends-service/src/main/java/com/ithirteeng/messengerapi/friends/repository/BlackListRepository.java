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

@Repository
public interface BlackListRepository extends JpaRepository<BlockedUserEntity, UUID> {
    Optional<BlockedUserEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    @Query("SELECT b FROM BlockedUserEntity b WHERE b.fullName LIKE %:fullName% AND b.deleteNoteDate = null AND b.targetUserId = :targetUserId")
    List<BlockedUserEntity> findByFullNameLikeAndTargetUserId(@Param("fullName") String fullName, @Param("targetUserId") UUID targetUserId);

    Page<BlockedUserEntity> findAllByTargetUserId(UUID targetUserId, Pageable pageable);

    Boolean existsByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    @Modifying
    @Query("UPDATE BlockedUserEntity b SET b.fullName = :fullName WHERE b.addingUserId = :addingUserId")
    void updateFullNameByAddingUserId(@Param("addingUserId") UUID addingUserId, @Param("fullName") String fullName);

    Boolean existsByTargetUserIdAndAddingUserIdAndDeleteNoteDate(UUID targetUserId, UUID addingUserId, Date deleteNoteDate);
}
