package com.spring.cloud.gateway;

import com.spring.cloud.gateway.common.JWTProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

//TODO: should not use github lib
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureWireMock
class ApiGatewayApplicationTests {
    //curl --header "Authorization: sadasd" -GET -i http://localhost:7777/voucher/aaa
    //curl -GET -i http://localhost:7777/voucher/aaa

    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testUnAuthorizedRequest() {
        webTestClient
                .get().uri("/voucher/list")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(403)
                .jsonPath("$.message", "Un-Authorized");
    }

    @Test
    void testUnAuthorizationFailed() {
        webTestClient
                .get().uri("/voucher/list")
                .header("Authorization", "Bearer kashdkjhasdas.aksjdkgakjhsgdjhgajhsd-kjd")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(403)
                .jsonPath("$.message", "Invalid Token");
    }

    @Test
    void testRequestSuccess() {
        stubFor(get(urlEqualTo("/onboarding/point"))
                .willReturn(aResponse()
                        .withBody("{ \"userId\":\"CIF\", \"point\": 1254 }")
                        .withHeader("Content-Type", "application/json")));

        webTestClient
                .get().uri("/onboarding/point")
                .header("Authorization", "Bearer " + jwtProvider.generate("CIF"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("CIF")
                .jsonPath("$.point", 1254);
    }

    @Test
    void testRemoteServiceConnectionRefused() {
        webTestClient
                .get().uri("/voucher/list")
                .header("Authorization", "Bearer " + jwtProvider.generate("CIF"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(500)
                .jsonPath("$.message", "Remote Service Unavailable");
    }

}
