package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
}
