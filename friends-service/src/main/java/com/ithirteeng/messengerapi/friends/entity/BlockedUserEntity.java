package com.ithirteeng.messengerapi.friends.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blacklist_table")
public class BlockedUserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "add_note_date")
    @Temporal(TemporalType.DATE)
    private Date addNoteDate;

    @Column(name = "delete_note_date")
    @Temporal(TemporalType.DATE)
    private Date deleteNoteDate;

    @Column(name = "target_user")
    private UUID targetUserId;

    @Column(name = "adding_user")
    private UUID addingUserId;

    @Column(name = "full_name")
    private String fullName;
}
