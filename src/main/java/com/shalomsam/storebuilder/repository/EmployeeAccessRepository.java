package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.user.EmployeeAccess;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeAccessRepository extends ReactiveMongoRepository<EmployeeAccess, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
