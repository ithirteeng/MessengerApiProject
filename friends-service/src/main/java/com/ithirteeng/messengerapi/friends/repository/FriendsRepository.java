package com.ithirteeng.messengerapi.friends.repository;

import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {
     Optional<FriendEntity> getFriendEntitiesByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);
}
