package com.shalomsam.storebuilder.config;

import com.shalomsam.storebuilder.controller.OrganizationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RoutesConfig {

    public static String OrganizationPath = "/api/v1/organization";
    public static String IdPath = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> organizationRouter(OrganizationHandler organizationHandler) {
        return RouterFunctions
                .route(
                    GET(OrganizationPath),
                    organizationHandler::getAll
                )
                .andRoute(
                    GET(OrganizationPath + IdPath),
                    organizationHandler::getById
                )
                .andRoute(
                    POST(OrganizationPath).and(accept(MediaType.APPLICATION_JSON)),
                    organizationHandler::create)
                .andRoute(
                    PATCH(OrganizationPath + IdPath),
                    organizationHandler::updateById
                )
                .andRoute(
                    DELETE(OrganizationPath + IdPath),
                    organizationHandler::deleteById
                );
    }
}
