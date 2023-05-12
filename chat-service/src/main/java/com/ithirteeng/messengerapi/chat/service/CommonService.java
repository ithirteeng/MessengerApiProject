package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

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
}