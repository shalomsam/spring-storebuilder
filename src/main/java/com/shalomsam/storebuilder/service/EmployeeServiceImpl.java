package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.shop.Discount;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.Employee;
import com.shalomsam.storebuilder.domain.user.EmployeeAddress;
import com.shalomsam.storebuilder.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements DomainService<Employee> {

    private final EmployeeRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public EmployeeServiceImpl(EmployeeRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Employee> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Employee> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Employee> create(Employee entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Employee> updateById(String id, Employee entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getFirstName() != null) update.set("fistName", entity.getFirstName());
        if (entity.getLastName() != null) update.set("lastName", entity.getLastName());
        if (entity.getEmail() != null) update.set("email", entity.getEmail());
        if (entity.getAuthToken() != null) update.set("authToken", entity.getAuthToken());
        if (entity.getPhoneNumber() != null) update.set("phoneNumber", entity.getPhoneNumber());
        if (entity.getOrganizationId() != null) update.set("organizationId", entity.getOrganizationId());
        if (entity.getEmployeeAccessId() != null) update.set("employeeAccessId", entity.getEmployeeAccessId());
        if (entity.getEmployeeAddressId() != null) update.set("employeeAddressId", entity.getEmployeeAddressId());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Employee.class
        ).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Employee> updateMany(List<Employee> entities) {
        return repository.saveAll(entities).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return repository.deleteById(id).thenReturn(1);
    }

    @Override
    public Mono<Integer> deleteManyById(List<String> ids) {
        return repository.deleteAllByIdIn(ids);
    }

    @Override
    public Mono<Long> getCount() {
        return repository.count();
    }

    public Mono<Employee> zipWithRelatedEntities(Employee employee) {
        Mono<Organization> organizationMono = mongoTemplate.findById(employee.getOrganizationId(), Organization.class);
        Mono<EmployeeAddress> addressMono = mongoTemplate.findById(employee.getEmployeeAccessId(), EmployeeAddress.class);

        return Mono.zip(organizationMono, addressMono, (o, a) -> {
            employee.setOrganization(o);
            employee.setEmployeeAddress(a);
            return employee;
        });
    }
}
