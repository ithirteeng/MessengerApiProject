package com.ithirteeng.messengerapi.friends.repository;

import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendsRepository extends JpaRepository<FriendEntity, UUID> {
     Optional<FriendEntity> findByTargetUserIdAndAddingUserId(UUID targetUserId, UUID addingUserId);

     Optional<List<FriendEntity>> getAllByTargetUserId(UUID targetUserId);

}
