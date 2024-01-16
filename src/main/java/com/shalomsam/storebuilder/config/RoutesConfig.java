package com.shalomsam.storebuilder.config;

import com.shalomsam.storebuilder.controller.GenericDomainHandler;
import com.shalomsam.storebuilder.controller.OrganizationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RoutesConfig {

    public static Map<String, String> DomainPaths = new HashMap<>() {{
        //put("organization", "/api/v1/organization");
        put("product", "/api/v1/product");
        put("review", "/api/v1/review");
        put("seller", "/api/v1/seller");
        put("employee", "/api/v1/employee");
        put("customer", "/api/v1/customer");
        put("cart", "/api/v1/cart");
        put("order", "/api/v1/order");
    }};

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
                PUT(OrganizationPath + IdPath),
                organizationHandler::updateById
            )
            .andRoute(
                DELETE(OrganizationPath + IdPath),
                organizationHandler::deleteById
            );
    }

    public RouterFunction<ServerResponse> genericRouter(GenericDomainHandler<?> genericDomainHandler) {
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().build();

        DomainPaths.forEach((domain, path) -> {
            routerFunction.andRoute(
                GET(path),
                genericDomainHandler.getAll(domain)
            );
        });
        return routerFunction;
    }
}
