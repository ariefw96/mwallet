package com.sendiri.mwallet_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MwalletTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwalletTransactionApplication.class, args);
	}

}
