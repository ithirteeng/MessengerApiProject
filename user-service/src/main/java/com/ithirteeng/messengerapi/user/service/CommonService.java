package com.ithirteeng.messengerapi.user.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}
