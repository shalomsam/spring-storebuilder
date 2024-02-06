package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.BaseIntegrationTest;
import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.service.DomainService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

public class GenericHandlerIntegrationTest extends BaseIntegrationTest {
    public static final SecurityMockServerConfigurers.JwtMutator AUTHORITIES = mockJwt().authorities(new SimpleGrantedAuthority("user"));

    public static final int MOCK_SIZE = 10;

    @Autowired
    private List<DomainService<?>> domainServices;

    public static Stream<String> getEntitySource() {
        Map<String, String> domainPaths = RoutesConfig.DomainPaths;
        return domainPaths.keySet().stream();
    }

    private Optional<DomainService<?>> getDomainService(String entityName) {
        try {
            String domainServiceName = String.format("{%s}ServiceImpl", entityName.toUpperCase());
            @SuppressWarnings("unchecked")
            Class<DomainService<?>> clazz = (Class<DomainService<?>>) Class.forName(domainServiceName);
            return domainServices.stream().filter(clazz::isInstance).findFirst();
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @ParameterizedTest
    @MethodSource("getEntitySource")
    public void testGetAllForEntitiesReturnsCollection(String entityName) {
        // given - mock organization is loaded into the mongodb container

        // when - client/browser performs GET request on /api/v1/organization endpoint
        WebTestClient.ResponseSpec response = webTestClient
            .mutateWith(csrf())
            .mutateWith(AUTHORITIES)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(RoutesConfig.DomainPaths.get(entityName))
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange();

        // then - verifying that returned JSON contains list of organizations
        WebTestClient.BodyContentSpec responseBody = response
            .expectStatus()
            .isOk()
            .expectBody()
            .consumeWith(System.out::println);

        responseBody
            .jsonPath("$.status").isEqualTo("success");

        if (Objects.equals(entityName, "productVariant")) {
            responseBody.jsonPath("$." + entityName).value(hasSize(MOCK_SIZE * 2));
        } else if (Objects.equals(entityName, "category")) {
            responseBody.jsonPath("$." + entityName).value(hasSize(MOCK_SIZE * 6));
        } else {
            responseBody.jsonPath("$." + entityName).value(hasSize(MOCK_SIZE));
        }
    }

}
