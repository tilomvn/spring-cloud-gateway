package com.spring.cloud.gateway;

import com.spring.cloud.gateway.client.AuthenticationClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(AuthenticationClient authenticationClient) {
		return args -> {
			System.out.println(authenticationClient.healthCheck());
			System.out.println(authenticationClient.healthCheck());
		};
	}
}
