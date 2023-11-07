package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.shop.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
