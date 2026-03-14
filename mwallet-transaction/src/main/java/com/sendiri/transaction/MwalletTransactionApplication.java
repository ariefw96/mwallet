package com.sendiri.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sendiri.transaction", "com.sendiri.repo"})
@EntityScan(basePackages = "com.sendiri.repo.entity")
@EnableKafka
public class MwalletTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwalletTransactionApplication.class, args);
    }

}
