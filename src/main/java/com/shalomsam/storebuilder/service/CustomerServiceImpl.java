package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class CustomerServiceImpl implements DomainService<Customer> {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.repository = customerRepository;
    }

    @Override
    public Flux<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Customer> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Customer> create(Customer entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Customer> updateById(String id, Customer entity) {
        return repository.findById(id)
            .map(customer -> {

                if (entity.getCustomerAccess() != null) customer.setCustomerAccess(entity.getCustomerAccess());
                // Todo: Test if cascading save implementation is needed for lists and/or Objects
                if (entity.getAddresses() != null) customer.getAddresses().addAll(entity.getAddresses());
                if (entity.getOrders() != null) customer.getOrders().addAll(entity.getOrders());
                if (entity.getPaymentMethods() != null) customer.getPaymentMethods().addAll(entity.getPaymentMethods());
                if (entity.getReviews() != null) customer.getReviews().addAll(entity.getReviews());
                if (entity.getTransactions() != null) customer.getTransactions().addAll(entity.getTransactions());

                return customer;
            })
            .flatMap(repository::save);
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
}
