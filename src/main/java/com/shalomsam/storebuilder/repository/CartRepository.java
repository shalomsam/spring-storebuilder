package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {
}
