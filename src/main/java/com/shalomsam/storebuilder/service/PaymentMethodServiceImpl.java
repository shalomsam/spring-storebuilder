package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethodType;
import com.shalomsam.storebuilder.domain.shop.Inventory;
import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.repository.PaymentMethodRepository;
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
public class PaymentMethodServiceImpl implements DomainService<PaymentMethod> {

    private final PaymentMethodRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public PaymentMethodServiceImpl(PaymentMethodRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<PaymentMethod> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<PaymentMethod> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<PaymentMethod> create(PaymentMethod entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<PaymentMethod> updateById(String id, PaymentMethod entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getCustomerId() != null) update.set("customerId", entity.getCustomerId());
        if (entity.getPaymentMethodType() != null) update.set("paymentMethodType", entity.getPaymentMethodType());

        if (entity.getPaymentMethodType() != null) {
            if (entity.getPaymentMethodType().equals(PaymentMethodType.CARD.name()) && entity.getCardDetails() != null) {
                update.set("cardDetails", entity.getCardDetails());
            }

            if (entity.getPaymentMethodType().equals(PaymentMethodType.CASH.name()) && entity.getCashAmount() != null) {
                update.set("cashAmount", entity.getCashAmount());
            }

            if (entity.getPaymentMethodType().equals(PaymentMethodType.STRIPE.name()) && entity.getStripeDetails() != null) {
                update.set("stripeDetails", entity.getStripeDetails());
            }
        }

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            PaymentMethod.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<PaymentMethod> updateMany(List<PaymentMethod> entities) {
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

    private Mono<PaymentMethod> zipWithRelatedEntities(PaymentMethod paymentMethod) {
        Mono<Customer> customerMono = mongoTemplate.findById(paymentMethod.getCustomerId(), Customer.class);

        return customerMono.map(customer -> {
            paymentMethod.setCustomer(customer);
            return paymentMethod;
        });
    }
}
