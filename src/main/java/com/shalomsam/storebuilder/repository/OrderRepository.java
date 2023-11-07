package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
