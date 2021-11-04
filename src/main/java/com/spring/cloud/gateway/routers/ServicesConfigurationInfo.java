package com.spring.cloud.gateway.routers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:services.yaml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties
public class ServicesConfigurationInfo {

    final Logger log = LoggerFactory.getLogger(ServicesConfigurationInfo.class);

    final GlobalServicesConfigurationInfo globalServicesConfigurationInfo;
    List<Service> services;

    @PostConstruct
    void loadGlobalConfig() {
        if (globalServicesConfigurationInfo.globalServices.size() > 0) {
            services = globalServicesConfigurationInfo.globalServices;
        }
    }

    ServicesConfigurationInfo(GlobalServicesConfigurationInfo globalServicesConfigurationInfo) {
        this.globalServicesConfigurationInfo = globalServicesConfigurationInfo;
    }

    public void setServices(List<Service> services) { this.services = services; }
    public List<Service> getServices() { return services; }

    public static class Service {
        String id;
        String endpoint;
        String host;
        boolean needAuthorize;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }

        public boolean getNeedAuthorize() { return needAuthorize; }
        public void setNeedAuthorize(boolean needAuthorize) { this.needAuthorize = needAuthorize; }
    }
}
