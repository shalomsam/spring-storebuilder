package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.model.user.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Mono<Integer> deleteAllByIdIn(Iterable<String> ids);
}
