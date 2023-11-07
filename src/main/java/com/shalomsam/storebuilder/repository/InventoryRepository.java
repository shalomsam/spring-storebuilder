package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Inventory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends ReactiveMongoRepository<Inventory, String> {
}
