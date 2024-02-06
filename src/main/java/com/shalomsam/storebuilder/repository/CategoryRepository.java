package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
