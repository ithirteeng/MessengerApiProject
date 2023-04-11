package com.ithirteeng.messengerapi.user;


import com.ithirteeng.messengerapi.common.security.EnableSpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableSpringSecurity
@ConfigurationPropertiesScan("com.ithirteeng.messengerapi")
@SpringBootApplication
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
