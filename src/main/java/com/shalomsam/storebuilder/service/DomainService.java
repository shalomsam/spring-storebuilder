package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.Organization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DomainService<T> {
    Flux<T> getAll();

    Mono<T> getById(String id);

    Mono<T> create(T entity);

    Mono<T> updateById(String id, T partialEntity);

    Flux<T> updateMany(List<T> entities);

    Mono<T> deleteById(String id);
}
