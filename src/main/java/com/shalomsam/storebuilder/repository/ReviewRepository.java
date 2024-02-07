package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
