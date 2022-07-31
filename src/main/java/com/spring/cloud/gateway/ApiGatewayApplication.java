package com.spring.cloud.gateway;

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
	ApplicationRunner runner(RestTemplate clientRestTemplate) {
		return args -> {
			var response = clientRestTemplate.getForObject("http://cornerstone-crm-cloud-authentication/health-check", String.class);
			System.out.println(response);
			var response1 = clientRestTemplate.getForObject("http://cornerstone-crm-cloud-authentication/health-check", String.class);
			System.out.println(response1);
		};
	}
}
