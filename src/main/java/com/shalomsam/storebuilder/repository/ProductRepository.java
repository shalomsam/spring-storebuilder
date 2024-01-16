package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
