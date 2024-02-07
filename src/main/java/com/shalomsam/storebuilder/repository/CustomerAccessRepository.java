package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.user.CustomerAccess;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerAccessRepository extends ReactiveMongoRepository<CustomerAccess, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
