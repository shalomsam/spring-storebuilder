package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Inventory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InventoryRepository extends ReactiveMongoRepository<Inventory, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
