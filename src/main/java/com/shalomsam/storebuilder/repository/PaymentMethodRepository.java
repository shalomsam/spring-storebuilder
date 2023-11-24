package com.shalomsam.storebuilder.repository;

import com.shalomsam.storebuilder.domain.paymentmethods.Card;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethodType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PaymentMethodRepository extends ReactiveMongoRepository<PaymentMethod, String> {
    Flux<? extends PaymentMethod> findByPaymentMethodType(PaymentMethodType paymentMethodType);

}
