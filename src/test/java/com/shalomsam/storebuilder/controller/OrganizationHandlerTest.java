package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.config.JacksonZonedDateTimeConfig;
import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import com.shalomsam.storebuilder.repository.OrganizationRepository;
import com.shalomsam.storebuilder.service.OrganizationServiceImpl;
import com.shalomsam.storebuilder.testUtils.OrganizationMockGeneratorService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.org.apache.commons.lang3.SerializationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;


@SpringBootTest
@AutoConfigureWebTestClient
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@Import({JacksonZonedDateTimeConfig.class, RoutesConfig.class, OrganizationMockGeneratorService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrganizationHandlerTest {

    public static final SecurityMockServerConfigurers.JwtMutator AUTHORITIES = mockJwt().authorities(new SimpleGrantedAuthority("user"));

    public static final int MOCK_SIZE = 10;

    @Autowired
    private OrganizationMockGeneratorService organizationMockGeneratorService;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private OrganizationRepository organizationRepository;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @MockBean
    private OrganizationServiceImpl organizationService;

    @InjectMocks
    private OrganizationHandler organizationHandler;

    private List<Organization> mockOrganizations;

    @BeforeAll
    public void setUp() {
        mockOrganizations = organizationMockGeneratorService.generateMock(MOCK_SIZE);
    }

    @Test
    public void testOrganizationGetAll() {

        // given - precondition generating mock organization data;
        Mockito.when(organizationService.getAll()).thenReturn(Flux.fromIterable(mockOrganizations));

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
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.organizations", hasSize(MOCK_SIZE));
    }

    @Test
    public void testOrganizationGetById() {
        // given - precondition generate a single mock organization
        Organization mockOrganization = mockOrganizations.get(0);
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
        bodyContentSpec.jsonPath("$.organization._id").isEqualTo(mockOrganization.getId());
        bodyContentSpec.jsonPath("$.organization.name").isEqualTo(mockOrganization.getName());
//        bodyContentSpec.jsonPath("$.organization.auditMetadata.createdAt")
//            .value(v -> mockOrganization.getAuditMetadata().getCreatedAt().isEqual(ZonedDateTime.parse(v.toString())));
//        bodyContentSpec.jsonPath("$.organization.auditMetadata.updatedAt")
//            .value(v -> mockOrganization.getAuditMetadata().getCreatedAt().isEqual(ZonedDateTime.parse(v.toString())));
    }

    @Test
    public void testOrganizationGetByNonExistentIdReturnNotFound() {
        // given - setup mocked repository to respond with Optional.Empty
        String nonExistingId = "NON_EXISTING_ID";
        Mockito.when(organizationService.getById(nonExistingId)).thenReturn(Mono.empty());

        // when - client/browser performs GET request on /api/v1/organization/{id} endpoint
        WebTestClient.ResponseSpec response = this.webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + nonExistingId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // then - should return a Not Found / 404 JSON response
        response
            .expectStatus()
            .isNotFound()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("error")
            .jsonPath("$.message").isEqualTo("Not Found");
    }

    @Test
    public void testCreateNewOrganizationReturnANewOrganization() {
        // given - new Organization to add
        List<Address> addressList = new ArrayList<>();
        addressList.add(
            Address.builder()
                .city("Vancouver")
                .state("British Columbia")
                .buildingNumber("11111")
                .street("Seymore Street")
                .country("Canada")
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

        Organization acmeCorpClone = SerializationUtils.clone(acmeCorporation);
        ObjectId mockId = new ObjectId();
        acmeCorpClone.setId(mockId.toString());

        AuditMetadata auditMetadata = new AuditMetadata();
        ZonedDateTime mockCreatedAt = ZonedDateTime.now();
        auditMetadata.setCreatedAt(mockCreatedAt);
        acmeCorpClone.setAuditMetadata(auditMetadata);
        Mockito.when(organizationService.create(Mockito.any(Organization.class))).thenReturn(Mono.just(acmeCorpClone));

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
            .jsonPath("$.organization._id").isEqualTo(mockId.toString());
//            .jsonPath("$.organization.auditMetadata.createdAt").value(v -> mockCreatedAt.isEqual(ZonedDateTime.parse(v.toString())));
    }

    @Test
    public void testUpdateOrganizationByIdReturnsUpdatedOrganization() {
        // given - organization exists
        String updateUrl = "http://newUrl.com";
        Organization mockOrganization = mockOrganizations.get(0);
        mockOrganization.setShopUrl(updateUrl);
        String mockId = mockOrganization.getId();

        Mockito.when(reactiveMongoTemplate.findAndModify(
                ArgumentMatchers.any(Query.class),
                ArgumentMatchers.any(Update.class),
                ArgumentMatchers.any(FindAndModifyOptions.class),
                ArgumentMatchers.<Class<Organization>>any()))
            .thenReturn(Mono.just(mockOrganization));

        // when - new updates are send via request body
        Organization orgUpdates = Organization.builder()
            .shopUrl(updateUrl)
            .build();

        Mockito.when(organizationService.updateById(Mockito.anyString(), Mockito.any(Organization.class))).thenReturn(Mono.just(mockOrganization));

        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .put()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.OrganizationPath + "/" + mockId)
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(orgUpdates)
            .exchange();

        // then - should return updated organization
        response
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.status").isEqualTo("success")
            .jsonPath("$.organization").exists()
            .jsonPath("$.organization._id").isEqualTo(mockId)
            .jsonPath("$.organization.shopUrl").isEqualTo(updateUrl);
    }

    @Test
    public void testDeleteOrganizationByIdDeletesOrg() {
        // given - organization exists
        List<Organization> mockOrganizationList = new ArrayList<>(mockOrganizations);
        String firstMockId = mockOrganizationList.get(0).getId();

        int mockSizeToReturn = mockOrganizations.size() - 1;
        Mono<Long> mockSizeMono = Mono.just((long) mockSizeToReturn);
        Mockito.when(organizationService.deleteById(Mockito.anyString())).thenReturn(Mono.just(1));
        Mockito.when(organizationService.getCount()).thenReturn(mockSizeMono);

        // when - Delete request is sent to the server
        WebTestClient.ResponseSpec response = webTestClient
                .mutateWith(csrf())
                .mutateWith(AUTHORITIES)
                .delete()
                .uri(uriBuilder -> uriBuilder
                        .path(RoutesConfig.OrganizationPath + "/" + firstMockId)
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
                .jsonPath("$.deleted").isEqualTo(firstMockId)
                .jsonPath("$.count").isEqualTo(mockSizeToReturn);
    }
}
