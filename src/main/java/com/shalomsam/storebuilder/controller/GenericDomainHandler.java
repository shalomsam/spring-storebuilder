package com.shalomsam.storebuilder.controller;

import com.shalomsam.storebuilder.dto.ApiResponse;
import com.shalomsam.storebuilder.dto.ErrorResponseDto;
import com.shalomsam.storebuilder.dto.SuccessResponseDto;
import com.shalomsam.storebuilder.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenericDomainHandler<T> {

    private final List<DomainService<T>> domainServices;

    public GenericDomainHandler(List<DomainService<T>> domainServices) {
        this.domainServices = domainServices;
    }

    private Optional<DomainService<T>> getDomainService(String entityName) {
        try {
            String domainServiceName = String.format("com.shalomsam.storebuilder.service.%sServiceImpl", entityName.substring(0, 1).toUpperCase() + entityName.substring(1));
            @SuppressWarnings("unchecked")
            Class<DomainService<T>> clazz = (Class<DomainService<T>>) Class.forName(domainServiceName);
            return domainServices.stream().filter(clazz::isInstance).findFirst();
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public HandlerFunction<ServerResponse> getAll(String domainServiceName) {
        Optional<DomainService<T>> domainService = this.getDomainService(domainServiceName);
        return request -> domainService
            .orElseThrow(() -> new RuntimeException("DomainService Not Found"))
            .getAll()
            .collectList()
            .flatMap(entity -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                    new SuccessResponseDto<List<T>>()
                        .addData(domainServiceName, entity)
                        .status("success")
                ))
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
