//package kh.com.sathapana.loyalty.gateway.routers;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties({GlobalServicesConfigurationInfo.class})
//@PropertySource(value = "classpath:global_services.yaml", factory = YamlPropertySourceFactory.class)
//class GlobalServicesConfigurationInfoTests {
//
//    @Autowired
//    GlobalServicesConfigurationInfo globalServicesConfigurationInfo;
//
//    @Test
//    void verifyLoadedValues() {
//        assertTrue(globalServicesConfigurationInfo.globalServices.size() > 0);
//        assertEquals("onboarding_global", globalServicesConfigurationInfo.globalServices.get(0).getId());
//    }
//}
