package com.ithirteeng.messengerapi.storage.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity данных о файле
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilesEntity {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "fileSize")
    private Integer fileSize;

}
