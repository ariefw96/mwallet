package com.sendiri.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sendiri.user", "com.sendiri.repo"})
@EntityScan(basePackages = "com.sendiri.repo.entity")
public class MwalletUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwalletUserApplication.class, args);
    }

}
