package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.BaseIntegrationTest;
import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.model.Organization;
import com.shalomsam.storebuilder.model.user.Address;
import com.shalomsam.storebuilder.model.user.ContactInfo;
import com.shalomsam.storebuilder.service.DomainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

public class OrganizationHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DomainService<Organization> organizationDomainService;

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
                .consumeWith(System.out::println)
                .jsonPath("$.status").isEqualTo("success")
                .jsonPath("$.organizations", hasSize(MOCK_SIZE));
    }

    @Test
    public void getOrganizationByIdShouldReturnSpecificOrganization() {
        // given - mock organization is loaded into the mongodb container
        List<Organization> organizations = organizationDomainService.getAll().collectList().block();
        String firstId = organizations.get(0).getId();

        // when - client/browser performs GET request on /api/v1/organization/{id} endpoint
        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + firstId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // then - verifying that returned JSON contains list of organizations
        response
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.organization", organizations.get(0));
    }

    @Test
    public void getOrganizationWithInvalidIdShouldReturnNotFound() {
        // given - mock organization is loaded into the mongodb container
        String invalidId = "InvalidId";

        // when - client/browser performs GET request on /api/v1/organization/InvalidId endpoint
        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + invalidId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // then - verifying that returned JSON contains list of organizations
        response
            .expectStatus()
            .isNotFound()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("error")
            .jsonPath("$.message").isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    public void createOrganizationWithValidDataShouldCreateAndReturnNewOrg() {
        // given - new Organization to add
        List<Address> addressList = new ArrayList<>();
        addressList.add(
            Address.builder()
                .unit("#43")
                .city("Vancouver")
                .state("British Columbia")
                .buildingNumber("11111")
                .street("Seymore Street")
                .country("Canada")
                .postalCode("X45 V3N")
                .build()
        );

        Organization acmeCorporation = Organization.builder()
            .name("Acme Corporation")
            .shopUrl("http://test.acmecorp.com")
            .contactInfo(
                ContactInfo.builder()
                    .email("info@acmecorp.com")
                    .phone("777999888")
                    .addresses(addressList)
                    .build()
            )
            .build();

        // when - client makes a request with new acmeCorporation json body
        WebTestClient.ResponseSpec response = this.webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .post()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(acmeCorporation)
            .exchange();

        // then - should return success response with created acmeCorporation object with ID
        response
            .expectStatus()
            .isCreated()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.organization").exists()
            .jsonPath("$.organization._id").exists()
            .jsonPath("$.organization.createdAt").exists();
    }
}
