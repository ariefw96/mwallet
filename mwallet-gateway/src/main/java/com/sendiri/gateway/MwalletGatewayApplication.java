package com.sendiri.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {
        "com.sendiri.mwallet_gateway",
        "com.sendiri.mwallet_repo"
})
public class MwalletGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MwalletGatewayApplication.class, args);
    }

}
