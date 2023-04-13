package com.ithirteeng.messengerapi.friends.repository;

import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {
     Optional<FriendEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

     @Query("SELECT f FROM FriendEntity f WHERE f.fullName LIKE %:fullName% AND f.deleteFriendDate = null")
     List<FriendEntity> findByFullNameLike(@Param("fullName") String fullName);



}
