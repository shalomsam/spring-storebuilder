package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.Offer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface OfferRepository extends ReactiveMongoRepository<Offer, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
