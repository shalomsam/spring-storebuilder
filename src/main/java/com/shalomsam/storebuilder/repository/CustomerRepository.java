package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.user.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
