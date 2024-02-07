package com.shalomsam.storebuilder.config;

import com.mongodb.lang.NonNull;
import com.shalomsam.storebuilder.controller.GenericDomainHandler;
import com.shalomsam.storebuilder.controller.OrganizationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RoutesConfig {

    public static Map<String, String> DomainPaths = new HashMap<>() {{
        put("cart", "/api/v1/cart");
        put("category", "/api/v1/category");
        put("customer", "/api/v1/customer");
        put("discount", "/api/v1/discount");
        //put("employee", "/api/v1/employee"); TODO: implement mock generator
        put("inventory", "/api/v1/inventory");
        put("offer", "/api/v1/offer");
        //put("order", "/api/v1/order"); TODO: implement mock generator
        //put("organization", "/api/v1/organization");
        put("product", "/api/v1/product");
        put("productVariant", "/api/v1/productvariant");
        //put("review", "/api/v1/review"); TODO: implement mock generator
        put("seller", "/api/v1/seller");
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

    @Bean
    public RouterFunction<ServerResponse> genericRouter(GenericDomainHandler<?> genericDomainHandler) {
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route(
            GET("/"),
            new HandlerFunction<>() {
                @NonNull
                @Override
                public Mono<ServerResponse> handle(@NonNull ServerRequest request) {
                    return ServerResponse.ok().bodyValue("Application Running");
                }
            }
        );

        for (Map.Entry<String, String> entry : DomainPaths.entrySet()) {
            String entityName = entry.getKey();
            String path = entry.getValue();

            routerFunction = routerFunction.andRoute(
                GET(path),
                genericDomainHandler.getAll(entityName)
            );
        }

        return routerFunction;
    }
}
