package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.BaseIntegrationTest;
import com.shalomsam.storebuilder.config.RoutesConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

public class OrganizationHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllOrganizationReturnsListOfOrgJson() {
        // given - mock organization is loaded into the mongodb container

        // when - client/browser performs GET request on /api/v1/organization endpoint
        WebTestClient.ResponseSpec response = webTestClient
                .mutateWith(csrf())
                .mutateWith(AUTHORITIES)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(RoutesConfig.OrganizationPath)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verifying that returned JSON contains list of organizations
        response
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.organizations", hasSize(MOCK_SIZE));

    }
}
