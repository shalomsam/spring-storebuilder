package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Seller;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends ReactiveMongoRepository<Seller, String> {
}
