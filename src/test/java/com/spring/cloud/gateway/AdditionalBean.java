package com.spring.cloud.gateway;

import com.spring.cloud.gateway.routers.GlobalServicesConfigurationInfo;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AdditionalBean {

    @Bean
    GlobalServicesConfigurationInfo globalServicesConfigurationInfo() {
        return new GlobalServicesConfigurationInfo();
    }
}
