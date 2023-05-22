package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.mapper.FileMapper;
import com.ithirteeng.messengerapi.chat.repository.FileRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с файлами
 */
@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    private final MessageRepository messageRepository;

    private final CommonService commonService;

    /**
     * Метод для создания записей в БД и прикрелпения файлов к сообщению
     *
     * @param filesIdsList список идентификаторов файлов, который получает пользователь, когда загружает файл
     * @param messageId     идентификатор сообщения, к которому нужно прикрепить файлы
     * @throws NotFoundException в случае, когда сообщение не найдено
     */
    public void attachFilesToMessage(List<UUID> filesIdsList, UUID messageId) {
        for (UUID fileId : filesIdsList) {
            var messageEntity = messageRepository.findById(messageId)
                    .orElseThrow(() -> new NotFoundException("Такого сообщения не существует!"));
            var fileData = commonService.getFileData(fileId.toString());
            var entity = FileMapper.createNewFile(fileData, messageEntity);
            fileRepository.save(entity);
        }
    }
}
