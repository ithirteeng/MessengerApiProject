package com.ithirteeng.messengerapi.chat;

import com.ithirteeng.messengerapi.common.config.EnableRequestLogging;
import com.ithirteeng.messengerapi.common.security.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableSpringSecurity
@EnableRequestLogging
@ConfigurationPropertiesScan("com.ithirteeng.messengerapi")
@SpringBootApplication
public class ChatServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
    }
}
