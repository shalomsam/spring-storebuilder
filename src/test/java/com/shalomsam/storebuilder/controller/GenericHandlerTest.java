package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.config.JacksonZonedDateTimeConfig;
import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.testUtils.OrganizationMockGeneratorService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
@AutoConfigureWebTestClient
@Import({JacksonZonedDateTimeConfig.class, RoutesConfig.class, OrganizationMockGeneratorService.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenericHandlerTest {
    public static final SecurityMockServerConfigurers.JwtMutator AUTHORITIES = mockJwt().authorities(new SimpleGrantedAuthority("user"));

    public static final int MOCK_SIZE = 10;


}
