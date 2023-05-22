package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.file.ShowFileDto;
import com.ithirteeng.messengerapi.chat.entity.FileEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import com.ithirteeng.messengerapi.common.model.FileDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMapper {
    public static FileEntity createNewFile(FileDataDto fileDataDto, MessageEntity messageEntity) {
        return FileEntity.builder()
                .fileSize(Long.valueOf(fileDataDto.getFileSize()))
                .messageEntity(messageEntity)
                .fileName(fileDataDto.getFileName())
                .storageId(UUID.fromString(fileDataDto.getFileId()))
                .build();
    }

    public static ShowFileDto showFileDtoFromEntity(FileEntity entity) {
        return ShowFileDto.builder()
                .storageFileId(entity.getStorageId().toString())
                .name(entity.getFileName())
                .fileSize(entity.getFileSize().toString())
                .build();
    }

    public static List<ShowFileDto> mapEntitiesListToDtosList(List<FileEntity> list) {
        var resultList = new ArrayList<ShowFileDto>();

        for (FileEntity entity: list) {
            resultList.add(showFileDtoFromEntity(entity));
        }

        return resultList;
    }


}
