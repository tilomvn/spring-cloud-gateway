package com.spring.cloud.gateway.routers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.cloud.gateway.common.JWTProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureWireMock
class GlobalErrorWebExceptionHandlerTests {

    @MockBean
    ObjectMapper objectMapper;

    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testErrorOnCreatingErrorResponse() throws Exception {
        when(objectMapper.writeValueAsBytes(any())).thenThrow(JsonProcessingException.class);

        webTestClient
                .get().uri("/voucher/list")
                .header("Authorization", "Bearer " + jwtProvider.generate("CIF"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }
}
