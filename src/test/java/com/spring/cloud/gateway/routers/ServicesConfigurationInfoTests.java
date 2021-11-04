//package kh.com.sathapana.loyalty.gateway.routers;
//
//import kh.com.sathapana.loyalty.gateway.AdditionalBean;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {AdditionalBean.class})
//@EnableConfigurationProperties({ServicesConfigurationInfo.class})
//@PropertySource(value = "classpath:services.yaml", factory = YamlPropertySourceFactory.class)
//class ServicesConfigurationInfoTests {
//
//    @Autowired
//    ServicesConfigurationInfo servicesConfigurationInfo;
//
//    @Test
//    void verifyLoadedValues() {
//        assertTrue(servicesConfigurationInfo.getServices().size() > 0);
//        assertEquals("onboarding_test", servicesConfigurationInfo.getServices().get(0).getId());
//    }
//}
