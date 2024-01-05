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

import java.util.List;

@Slf4j
@Component
public class OrganizationHandler {

    private final DomainService<Organization> organizationService;


    public OrganizationHandler(DomainService<Organization> organizationService) {
        this.organizationService = organizationService;
    }

    public Mono<ServerResponse> getAll(ServerRequest ignoredServerRequest) {
        log.info("OrganizationHandler getAll method called.");
        return organizationService.getAll().collectList().flatMap(organizations -> {
            return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                    new SuccessResponseDto<List<Organization>>()
                        .addData("organizations", organizations)
                        .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
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
                    .bodyValue(
                        new SuccessResponseDto<Organization>()
                            .addData("organization", organization)
                            .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
                    )
            )
            .switchIfEmpty(
                ServerResponse
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        new ErrorResponseDto()
                            .setMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .status(ApiResponse.ApiResponseType.ERROR.getValue())
                    )
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
                ServerResponse
                    .status(HttpStatusCode.valueOf(500))
                    .bodyValue(
                        new ErrorResponseDto()
                            .status(ApiResponse.ApiResponseType.ERROR.getValue())
                            .setMessage(e.getMessage())
                    );
            })
            .flatMap(organization ->
                ServerResponse.created(
                    UriComponentsBuilder
                        .fromPath(RoutesConfig.OrganizationPath + RoutesConfig.IdPath)
                        .buildAndExpand(organization.getId())
                        .toUri()
                )
                .bodyValue(
                    new SuccessResponseDto<Organization>()
                        .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
                        .addData("organization", organization)
                )
            );

    }

    public Mono<ServerResponse> updateById(ServerRequest request) {
        String id = request.pathVariable("id");
        return request
            .bodyToMono(Organization.class)
            .flatMap(reqOrg -> {
                Mono<Organization> updatedOrgMono = organizationService.updateById(id, reqOrg);
                log.debug("updatedOrgMono : {} ", updatedOrgMono);
                return updatedOrgMono;
            })
            .flatMap(organization ->
                {
                    log.debug("Server flatmap: {} ", organization);
                    return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(
                            new SuccessResponseDto<Organization>()
                                .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
                                .addData("organization", organization)
                        )
                        .switchIfEmpty(
                            ServerResponse
                                .status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(
                                    new ErrorResponseDto()
                                        .setMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                                        .status(ApiResponse.ApiResponseType.ERROR.getValue())
                                )
                        );
                }
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
                        new SuccessResponseDto<>()
                            .status(ApiResponse.ApiResponseType.SUCCESS.getValue())
                            .addData("deleted", id)
                            .addData("count", organizationService.getCount().block())
                    )
            )
            .switchIfEmpty(
                ServerResponse
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                        new ErrorResponseDto()
                            .setMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .status(ApiResponse.ApiResponseType.ERROR.getValue())
                    )
            );
    }
}
