package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.model.shop.Order;
import com.shalomsam.storebuilder.model.shop.Transaction;
import com.shalomsam.storebuilder.model.user.Customer;
import com.shalomsam.storebuilder.repository.TransactionRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TransactionServiceImpl implements DomainService<Transaction> {

    private final TransactionRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public TransactionServiceImpl(TransactionRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Transaction> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Transaction> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Transaction> create(Transaction entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Transaction> updateById(String id, Transaction entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getCustomerId() != null) update.set("customerId", entity.getCustomerId());
        if (entity.getOrderId() != null) update.set("orderId", entity.getOrderId());
        if (entity.getPaymentMethodId() != null) update.set("paymentMethodId", entity.getPaymentMethodId());
        if (entity.getCurrencyCode() != null) update.set("currencyCode", entity.getCurrencyCode());
        if (entity.getAmount() != null) update.set("amount", entity.getAmount());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Transaction.class
        )
        .flatMap(this::zipWithRelatedEntities);
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

    private Mono<Transaction> zipWithRelatedEntities(Transaction transaction) {
        Mono<Customer> customerMono = mongoTemplate.findById(transaction.getCustomerId(), Customer.class);
        Mono<Order> orderMono = mongoTemplate.findById(transaction.getOrderId(), Order.class);
        Mono<PaymentMethod> paymentMethodMono = mongoTemplate.findById(transaction.getPaymentMethodId(), PaymentMethod.class);

        return Mono.zip(customerMono, orderMono, paymentMethodMono)
            .map(tuple -> {
                transaction.setCustomer(tuple.getT1());
                transaction.setOrder(tuple.getT2());
                transaction.setPaymentMethod(tuple.getT3());
                return transaction;
            });
    }
}
