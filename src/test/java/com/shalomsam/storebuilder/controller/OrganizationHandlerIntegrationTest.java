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
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

public class OrganizationHandlerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private DomainService<Organization> organizationDomainService;

    private Organization firstOrg;
    private Organization lastOrg;

    @Test
    public void testGetAllOrganizationReturnsListOfOrgJson() {
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
    public void testGetOrganizationByIdShouldReturnSpecificOrganization() {
        // given - mock organization is loaded into the mongodb container
        Organization firstOrg = getFirstOrg();
        String firstId = firstOrg.getId();

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
            .jsonPath("$.organization", firstOrg);
    }

    @Test
    public void testGetOrganizationWithInvalidIdShouldReturnNotFound() {
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
    public void testCreateOrganizationWithValidDataShouldCreateAndReturnNewOrg() {
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
            .jsonPath("$.organization.createdAt").exists()
            .jsonPath("$.organization.name").isEqualTo(acmeCorporation.getName())
            .jsonPath("$.organization.shopUrl").isEqualTo(acmeCorporation.getShopUrl())
            .jsonPath("$.organization.contactInfo").isEqualTo(acmeCorporation.getContactInfo())
            .jsonPath("$.organization.contactInfo.addresses.[0]").isEqualTo(addressList.get(0));
    }

    @Test
    public void testUpdateOrganizationByIdReturnsUpdatedOrganization() {
        // given - mock organization is loaded into the mongodb container
        String updateUrl = "http://newUrl.com";
        Organization firstOrg = getFirstOrg();
        String firstId = firstOrg.getId();

        Organization orgPartialUpdate = Organization.builder()
            .shopUrl(updateUrl)
            .build();

        // when - new updates are send via request body
        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .put()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + firstId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(orgPartialUpdate)
            .exchange();

        // then - should return updated organization
        response
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.organization").exists()
            .jsonPath("$.organization._id").isEqualTo(firstId)
            .jsonPath("$.organization.shopUrl").isEqualTo(updateUrl);

    }

    @Test
    public void testDeleteOrganizationByIdDeletesOrg() {
        // given - organization exists
        int countBeforeDelete = Objects.requireNonNull(organizationDomainService.getCount().block()).intValue();
        Organization lastOrg = getLastOrg();
        String lastOrgId = lastOrg.getId();

        // when - Delete request is sent to the server
        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .delete()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + lastOrgId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // then - delete should be called with correct ID and return
        response
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.deleted").isEqualTo(lastOrgId)
            .jsonPath("$.count").isEqualTo(countBeforeDelete - 1);


    }


    private Organization getFirstOrg() {
        if (firstOrg == null) {
            List<Organization> organizations = organizationDomainService.getAll().collectList().block();
            firstOrg = organizations.get(0);
            lastOrg = organizations.get(organizations.size() - 1);
        }

        return firstOrg;
    }

    private Organization getLastOrg() {
        if (lastOrg == null) {
            List<Organization> organizations = organizationDomainService.getAll().collectList().block();
            firstOrg = organizations.get(0);
            lastOrg = organizations.get(organizations.size() - 1);
        }

        return lastOrg;
    }
}
