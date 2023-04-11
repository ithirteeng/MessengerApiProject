package com.ithirteeng.messengerapi.user.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Entity пользователя
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_table")
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    private String city;

    @Column(name = "avatar_id")
    private UUID avatarId;

    private UserEntity(String fullName, String login, String email, String city, Date birthDate, String telephoneNumber) {
        this.fullName = fullName;
        this.login = login;
        this.email = email;
        this.city = city;
        this.birthDate = birthDate;
        this.telephoneNumber = telephoneNumber;
    }

    public static UserEntity from(String fullName, String login, String email, String city, Date birthDate, String telephoneNumber) {
        return new UserEntity(fullName, login, email, city, birthDate, telephoneNumber);
    }
}
