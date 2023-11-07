package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends ReactiveMongoRepository<ProductVariant, String> {
}
