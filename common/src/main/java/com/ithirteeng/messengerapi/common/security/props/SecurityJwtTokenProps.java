package com.ithirteeng.messengerapi.common.security.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для парсинга реквизитов из application.yml
 */
@Getter
@Setter
@ToString
public class SecurityJwtTokenProps {

    private String[] permitAll;

    private String secret;

    private Long expiration;

    private String rootPath;

}
