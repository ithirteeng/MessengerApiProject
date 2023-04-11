package com.ithirteeng.messengerapi.common.security.props;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
@Getter
@Setter
@ToString
public class SecurityProps {
    private SecurityJwtTokenProps jwtToken;

    private SecurityIntegrationsProps integrations;
}
