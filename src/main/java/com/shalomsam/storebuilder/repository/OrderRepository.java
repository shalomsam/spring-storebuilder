package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
