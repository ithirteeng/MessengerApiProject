package com.ithirteeng.messengerapi.storage.repository;

import com.ithirteeng.messengerapi.storage.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с данными о файлах
 */
public interface FilesRepository extends JpaRepository<FilesEntity, String> {
}
