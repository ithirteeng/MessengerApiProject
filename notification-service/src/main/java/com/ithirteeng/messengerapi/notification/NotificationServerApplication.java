package com.ithirteeng.messengerapi.notification;

import com.ithirteeng.messengerapi.common.config.EnableRequestLogging;
import com.ithirteeng.messengerapi.common.security.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableRequestLogging
@EnableSpringSecurity
@ConfigurationPropertiesScan("com.ithirteeng.messengerapi")
@SpringBootApplication
public class NotificationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServerApplication.class, args);
    }
}
