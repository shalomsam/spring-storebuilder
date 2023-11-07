package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Discount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends ReactiveMongoRepository<Discount, String> {
}
