package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.FileDataDto;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Сервис для отправки интеграционных запросов
 */
@Service
@RequiredArgsConstructor
public class CommonService {
    private final SecurityProps securityProps;

    /**
     * Вспомогательный метод для сборки {@link HttpEntity}<{@link Void}>
     *
     * @return {@link HttpEntity}<{@link Void}>
     */
    private HttpEntity<Void> setupRequestHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestsConstants.API_KEY_HEADER, securityProps.getIntegrations().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    /**
     * Метод для проверки существования пользователя по интеграционному запросу в сервис пользователей
     *
     * @param userId Id проверяемого пользователя
     * @throws NotFoundException в случае, если пользователь не был найден
     */
    public void checkUserExisting(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1301/integration/users/check/" + userId.toString();

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        if (Boolean.FALSE.equals(responseEntity.getBody())) {
            throw new NotFoundException("Пользователя с id: " + userId + " не существует!");
        }
    }

    /**
     * Метод проверки нахождения целевого пользовтеля в черном списке внешнего пользователя
     *
     * @param checkUserId  Id внешнего пользователя
     * @param targetUserId Id целевого пользователя
     */
    public void checkIfUserInBlackList(UUID checkUserId, UUID targetUserId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1308/integration/blacklist/" + checkUserId.toString() + "/" + targetUserId.toString();

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        if (Boolean.TRUE.equals(responseEntity.getBody())) {
            throw new BadRequestException("Пользователю с id: " + checkUserId + " находится в черном списке!");
        }
    }

    /**
     * Метод для проверки, являются ли пользователи друзьями
     *
     * @param externalUserId Id внешнего пользователя
     * @param targetUserId   Id целевого пользователя
     */
    public void checkIfUsersAreFriends(UUID externalUserId, UUID targetUserId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1308/integration/friends/" + externalUserId.toString() + "/" + targetUserId.toString();

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        if (Boolean.FALSE.equals(responseEntity.getBody())) {
            throw new BadRequestException("Пользователь не является вашим другом!");
        }
    }

    /**
     * Метод для получения данных о пользователе по интеграционному запросу в сервис пользователей
     *
     * @param userId Id пользователя
     */
    public UserDto getUserById(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1301/integration/users/data/" + userId.toString();

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), UserDto.class
        );

        return responseEntity.getBody();
    }

    /**
     * Метод проверки на существование файла в хранилище
     *
     * @param fileStorageId идентификатор файла в хранилище
     * @return {@link Boolean}
     */
    public Boolean checkIfFileExists(String fileStorageId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1311/integration/file/check/" + fileStorageId;

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        return responseEntity.getBody();
    }

    /**
     * Метод для получения данных о файле по идентификатору
     *
     * @param fileStorageId идентификатор файла в хранилище
     * @return {@link com.ithirteeng.messengerapi.common.model.FileDataDto}
     */
    public FileDataDto getFileData(String fileStorageId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1311/integration/file/" + fileStorageId;

        ResponseEntity<FileDataDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), FileDataDto.class
        );

        return responseEntity.getBody();
    }
}
