package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.config.JacksonZonedDateTimeConfig;
import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.service.DomainService;
import com.shalomsam.storebuilder.testUtils.MockOrganizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = OrganizationHandler.class)
@Import({JacksonZonedDateTimeConfig.class, RoutesConfig.class, MockOrganizationService.class})
public class OrganizationHandlerTest {

    public static final SecurityMockServerConfigurers.JwtMutator AUTHORITIES = mockJwt().authorities(new SimpleGrantedAuthority("user"));

    @Autowired
    private MockOrganizationService mockOrganizationService;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DomainService<Organization> organizationService;

    @Test
    public void testOrganizationGetAll() {

        // given - precondition generating mock organization data;
        int mockSize = 10;
        List<Organization> mockOrganization = mockOrganizationService.generateMock(mockSize);
        Mockito.when(organizationService.getAll()).thenReturn(Flux.fromIterable(mockOrganization));

        // when - client/browser performs GET request on /api/v1/organization endpoint
        WebTestClient.ResponseSpec response = this.webTestClient
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
                .jsonPath("@.status").isEqualTo("success")
                .jsonPath("@.organizations", hasSize(mockSize));
    }

    @Test
    public void testOrganizationGetById() {
        // given - precondition generate a single mock organization
        Organization mockOrganization = mockOrganizationService.generateMock(1).get(0);
        Mockito.when(organizationService.getById(Mockito.anyString())).thenReturn(Mono.just(mockOrganization));

        // when - client/browser performs GET request on /api/v1/organization/{id} endpoint
        WebTestClient.ResponseSpec response = this.webTestClient
                .mutateWith(csrf())
                .mutateWith(AUTHORITIES)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(RoutesConfig.OrganizationPath + "/" + mockOrganization.getId())
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify that returned json is the same mockOrganization
        WebTestClient.BodyContentSpec bodyContentSpec = response
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println);

        bodyContentSpec.jsonPath("$.status").isEqualTo("success");
        bodyContentSpec.jsonPath("$.organization.id").isEqualTo(mockOrganization.getId());
        bodyContentSpec.jsonPath("$.organization.name").isEqualTo(mockOrganization.getName());
        bodyContentSpec.jsonPath("$.organization.auditMetadata.createdAt").isEqualTo(mockOrganization.getAuditMetadata().getCreatedAt().toOffsetDateTime().toZonedDateTime().toString());
        bodyContentSpec.jsonPath("$.organization.auditMetadata.updatedAt").isEqualTo(mockOrganization.getAuditMetadata().getUpdatedAt().toOffsetDateTime().toZonedDateTime().toString());
    }

}
