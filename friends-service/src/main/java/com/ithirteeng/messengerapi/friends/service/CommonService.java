package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
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

    private HttpEntity<Void> setupRequestHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestsConstants.API_KEY_HEADER, securityProps.getIntegrations().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

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

    public UserDto getUserById(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1301/integration/users/data/" + userId.toString();

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), UserDto.class
        );

        return responseEntity.getBody();
    }
}
