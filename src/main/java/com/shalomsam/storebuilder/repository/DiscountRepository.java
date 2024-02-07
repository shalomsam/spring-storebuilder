package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.Discount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DiscountRepository extends ReactiveMongoRepository<Discount, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
