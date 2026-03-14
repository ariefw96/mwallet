package com.sendiri.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.sendiri.mwallet_repo.entity"
})
@ComponentScan(basePackages = {
        "com.sendiri.mwallet_repo",
        "com.sendiri.mwallet_user"
})
public class MwalletUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwalletUserApplication.class, args);
    }

}
