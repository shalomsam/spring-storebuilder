package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
