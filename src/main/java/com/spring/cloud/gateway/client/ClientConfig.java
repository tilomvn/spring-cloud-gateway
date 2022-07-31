package com.spring.cloud.gateway.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    //Enable Load Balancing When Using Eureka
    @LoadBalanced
    @Bean
    RestTemplate clientRestTemplate() { return new RestTemplate(); }
}
