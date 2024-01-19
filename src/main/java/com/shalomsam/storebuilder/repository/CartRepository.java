package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
