package com.ithirteeng.messengerapi.friends.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Example;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Entity записи в черном списке
 */
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

    private BlockedUserEntity(Date addNoteDate, Date deleteNoteDate, UUID addingUserId, UUID targetUserId) {
        this.addNoteDate = addNoteDate;
        this.deleteNoteDate = deleteNoteDate;
        this.addingUserId = addingUserId;
        this.targetUserId = targetUserId;
    }

    /**
     * Метод для получения {@link BlockedUserEntity} в целях получения объекта {@link Example}<{@link BlockedUserEntity}>
     */
    public static BlockedUserEntity from(Date addNoteDate, Date deleteNoteDate, UUID addingUserId, UUID targetUserId) {
        return new BlockedUserEntity(addNoteDate, deleteNoteDate, addingUserId, targetUserId);
    }
}
