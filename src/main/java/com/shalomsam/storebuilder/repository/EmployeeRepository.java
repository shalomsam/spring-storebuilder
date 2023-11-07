package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.user.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
