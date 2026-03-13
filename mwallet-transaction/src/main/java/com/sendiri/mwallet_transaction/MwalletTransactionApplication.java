package com.sendiri.mwallet_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EntityScan(basePackages = {
		"com.sendiri.mwallet_repo.entity"
})
@ComponentScan(basePackages = {
		"com.sendiri.mwallet_repo", "com.sendiri.mwallet_transaction"
})
@EnableKafka
public class MwalletTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwalletTransactionApplication.class, args);
	}

}
