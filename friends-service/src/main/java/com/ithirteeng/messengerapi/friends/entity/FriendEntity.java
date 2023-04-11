package com.ithirteeng.messengerapi.friends.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "friend_table")
public class FriendEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "add_friend_date")
    private Date addFriendDate;

    @Column(name = "delete_friend_date")
    private Date deleteFriendDate;

    @Column(name = "target_user")
    private UUID targetUserId;

    @Column(name = "adding_user")
    private UUID addingUserId;

    @Column(name = "full_name")
    private String fullName;
}
