package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.shop.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
