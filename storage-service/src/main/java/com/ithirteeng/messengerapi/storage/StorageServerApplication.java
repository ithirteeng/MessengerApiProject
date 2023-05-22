package com.ithirteeng.messengerapi.storage;

import com.ithirteeng.messengerapi.common.config.EnableRequestLogging;
import com.ithirteeng.messengerapi.common.security.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableRequestLogging

@EnableSpringSecurity
@ConfigurationPropertiesScan("com.ithirteeng.messengerapi")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class StorageServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageServerApplication.class, args);
    }
}

