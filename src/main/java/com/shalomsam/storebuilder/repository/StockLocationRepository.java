package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.StockLocation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StockLocationRepository extends ReactiveMongoRepository<StockLocation, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}

