package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.paymentmethods.Card;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentMethodRepository extends ReactiveMongoRepository<PaymentMethod, String> {
    Flux<Card> findByPaymentMethodTypeIsCard();
}
