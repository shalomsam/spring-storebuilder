package com.shalomsam.storebuilder.controller;


import com.shalomsam.storebuilder.config.RoutesConfig;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.dto.ApiResponse;
import com.shalomsam.storebuilder.dto.ErrorResponseDto;
import com.shalomsam.storebuilder.dto.SuccessResponseDto;
import com.shalomsam.storebuilder.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Component
public class OrganizationHandler {

    private final DomainService<Organization> organizationService;


    public OrganizationHandler(DomainService<Organization> organizationService) {
        this.organizationService = organizationService;
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        log.info("OrganizationHandler getAll method called.");
        return organizationService.getAll().collectList().flatMap(organizations -> {
            return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                    SuccessResponseDto
                        .builder()
                        .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
                        .data("organization", organizations)
                        .build()
                );
        });
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return organizationService.getById(id)
            .flatMap(organization ->
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(organization)
            )
            .switchIfEmpty(
                ServerResponse.notFound().build()
            );
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest
            .bodyToMono(Organization.class)
            //.doOnNext(validator::validate) // TODO: add validators
            .flatMap(organizationService::create)
            .doOnSuccess(organization -> log.info("New Organization created/saved: {}", organization.getId()))
            .doOnError(e -> {
                log.error("Failed to create new organization record: message={}", e.getMessage());
                ServerResponse.status(HttpStatusCode.valueOf(500)).bodyValue(e.getMessage());
            })
            .flatMap(organization ->
                ServerResponse.created(
                    UriComponentsBuilder
                        .fromPath(RoutesConfig.OrganizationPath + RoutesConfig.IdPath)
                        .buildAndExpand(organization.getId())
                        .toUri()
                )
                .bodyValue(organization)
            );

    }

    public Mono<ServerResponse> updateById(ServerRequest request) {
        String id = request.pathVariable("id");
        return request
            .bodyToMono(Organization.class)
            .flatMap(organization -> organizationService.updateById(id, organization))
            .flatMap(organization ->
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(organization)
                    .switchIfEmpty(
                        ServerResponse
                            .notFound()
                            .build()
                    )
            );
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        String id = request.pathVariable("id");

        return organizationService.deleteById(id)
            .then(
                ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        Objects
                            .requireNonNull(
                                new HashMap<>().put("deleted", id)
                            )
                    )
            )
            .switchIfEmpty(
                ServerResponse.notFound().build()
            );
    }
}
