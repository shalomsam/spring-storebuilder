package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethodType;
import com.shalomsam.storebuilder.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements DomainService<PaymentMethod> {

    private final PaymentMethodRepository repository;

    public PaymentMethodServiceImpl(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<PaymentMethod> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<PaymentMethod> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<PaymentMethod> create(PaymentMethod entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<PaymentMethod> updateById(String id, PaymentMethod entity) {
        return repository.findById(id)
            .map(paymentMethod -> {
                if (entity.getCustomer() != null) paymentMethod.setCustomer(entity.getCustomer());
                if (entity.getPaymentMethodType() != null) paymentMethod.setPaymentMethodType(entity.getPaymentMethodType());

                if (entity.getPaymentMethodType() != null) {
                    if (entity.getPaymentMethodType().equals(PaymentMethodType.CARD.name()) && entity.getCardDetails() != null) {
                        paymentMethod.setCardDetails(entity.getCardDetails());
                    }

                    if (entity.getPaymentMethodType().equals(PaymentMethodType.CASH.name()) && entity.getCashAmount() != null) {
                        paymentMethod.setCashAmount(entity.getCashAmount());
                    }

                    if (entity.getPaymentMethodType().equals(PaymentMethodType.STRIPE.name()) && entity.getStripeDetails() != null) {
                        paymentMethod.setStripeDetails(entity.getStripeDetails());
                    }
                }

                return paymentMethod;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<PaymentMethod> updateMany(List<PaymentMethod> entities) {
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
