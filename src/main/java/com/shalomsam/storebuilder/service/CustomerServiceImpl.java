package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.domain.user.CustomerAccess;
import com.shalomsam.storebuilder.domain.user.CustomerAddress;
import com.shalomsam.storebuilder.repository.CustomerRepository;
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
public class CustomerServiceImpl implements DomainService<Customer> {

    private final CustomerRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public CustomerServiceImpl(CustomerRepository customerRepository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = customerRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Customer> create(Customer entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Customer> updateById(String id, Customer entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getFirstName() != null) update.set("firstName", entity.getFirstName());
        if (entity.getLastName() != null) update.set("lastName", entity.getLastName());
        if (entity.getEmail() != null) update.set("email", entity.getEmail());
        if (entity.getAuthToken() != null) update.set("authToken", entity.getAuthToken());
        if (entity.getPhoneNumber() != null) update.set("phoneNumber", entity.getPhoneNumber());
        if (entity.getOrganizationId() != null) update.set("organizationId", entity.getOrganizationId());
        if (entity.getCustomerAccessId() != null) update.set("customerAccessId", entity.getCustomerAccessId());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Customer.class
        ).flatMap(this::zipWithRelatedEntities);

    }

    @Override
    public Flux<Customer> updateMany(List<Customer> entities) {
        return repository.saveAll(entities);
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

    public Mono<Customer> zipWithRelatedEntities(Customer customer) {
        Mono<CustomerAccess> customerAccessMono = mongoTemplate.findById(customer.getCustomerAccessId(), CustomerAccess.class);
        Mono<Organization> organizationMono = mongoTemplate.findById(customer.getOrganizationId(), Organization.class);

        return Mono.zip(customerAccessMono, organizationMono, (c, o) -> {
            customer.setCustomerAccess(c);
            customer.setOrganization(o);
            return customer;
        });
    }
}
