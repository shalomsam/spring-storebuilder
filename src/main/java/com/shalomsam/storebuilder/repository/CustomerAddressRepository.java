package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.user.CustomerAddress;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerAddressRepository extends ReactiveMongoRepository<CustomerAddress, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
