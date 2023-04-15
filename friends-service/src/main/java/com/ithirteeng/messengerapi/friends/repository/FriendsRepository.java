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

@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {

    Optional<FriendEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

    @Query("SELECT f FROM FriendEntity f WHERE f.fullName LIKE %:fullName% AND f.deleteFriendDate = null AND f.targetUserId = :targetUserId")
    List<FriendEntity> findByFullNameLikeAndTargetUserId(@Param("fullName") String fullName, @Param("targetUserId") UUID targetUserId);

    Page<FriendEntity> findAllByTargetUserId(UUID targetUserId, Pageable pageable);

    Boolean existsByAddingUserIdAndTargetUserId(UUID addingUserId, UUID targetUserId);

    @Modifying
    @Query("UPDATE FriendEntity f SET f.fullName = :fullName WHERE f.addingUserId = :addingUserId")
    void updateFullNameByAddingUserId(@Param("addingUserId") UUID addingUserId, @Param("fullName") String fullName);
}
