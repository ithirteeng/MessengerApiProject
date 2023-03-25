package com.ithirteeng.firstlabproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id")
    private String uuid;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String patronymic;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "registration_date")
    private Date registrationDate;

}
