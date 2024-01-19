package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Transaction;
import com.shalomsam.storebuilder.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TransactionServiceImpl implements DomainService<Transaction> {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Transaction> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Transaction> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Transaction> create(Transaction entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Transaction> updateById(String id, Transaction entity) {
        return repository.findById(id)
            .map(transaction -> {
                if (entity.getCustomer() != null) transaction.setCustomer(entity.getCustomer());
                if (entity.getOrder() != null) transaction.setOrder(entity.getOrder());
                if (entity.getPaymentMethod() != null) transaction.setPaymentMethod(entity.getPaymentMethod());
                if (entity.getAmount() != null) transaction.setAmount(entity.getAmount());

                return transaction;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Transaction> updateMany(List<Transaction> entities) {
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
