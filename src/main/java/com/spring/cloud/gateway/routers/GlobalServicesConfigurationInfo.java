package com.spring.cloud.gateway.routers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
//TODO: integrate to CI/CD
@PropertySource(value = "file:./opt/global_services.yaml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties
public class GlobalServicesConfigurationInfo {

    List<ServicesConfigurationInfo.Service> globalServices;

    public GlobalServicesConfigurationInfo() { globalServices = new ArrayList<>(); }

    public void setGlobalServices(List<ServicesConfigurationInfo.Service> services) { this.globalServices = services; }
    public List<ServicesConfigurationInfo.Service> getGlobalServices() { return globalServices; }
}
