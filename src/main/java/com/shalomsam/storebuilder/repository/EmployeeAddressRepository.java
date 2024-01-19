package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.user.EmployeeAddress;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeAddressRepository extends ReactiveMongoRepository<EmployeeAddress, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
