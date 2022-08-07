package com.spring.cloud.gateway.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationClient {

    private final String serviceId = "http://cornerstone-crm-cloud-authentication";

    final RestTemplate clientRestTemplate;

    AuthenticationClient(RestTemplate clientRestTemplate) {
        this.clientRestTemplate = clientRestTemplate;
    }

    public String healthCheck() {
        return clientRestTemplate.getForObject(serviceId + "/health-check", String.class);
    }
}
