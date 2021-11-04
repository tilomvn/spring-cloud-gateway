package com.spring.cloud.gateway.common;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CommonConfig {

    @Bean
    RestTemplateBuilder restTemplateBuilder() { return new RestTemplateBuilder(); }
}
