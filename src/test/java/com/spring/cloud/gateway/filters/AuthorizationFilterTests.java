package com.spring.cloud.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureWireMock
class AuthorizationFilterTests {

    @MockBean
    ObjectMapper objectMapper;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testErrorOnCreatingErrorResponse() throws Exception {
        when(objectMapper.writeValueAsBytes(any())).thenThrow(JsonProcessingException.class);

        stubFor(get(urlEqualTo("/onboarding/point"))
                .willReturn(aResponse()
                        .withBody("{ \"userId\":\"CIF\", \"point\": 1254 }")
                        .withHeader("Content-Type", "application/json")));

        webTestClient
                .get().uri("/onboarding/point")
                .header("Authorization", "Bearer fdsfsdfsd.ghrthrt-6gfdg")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().isEmpty();
    }
}
