package com.sendiri.mwallet_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MwalletUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MwalletUserApplication.class, args);
	}

}
