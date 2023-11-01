package com.shalomsam.storebuilder.domain;

import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Transaction {
    @MongoId
    private ObjectId id;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    @Field("orderId")
    @DocumentReference
    private Order order;

    @Field("paymentMethodId")
    @DocumentReference
    private PaymentMethod paymentMethod;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
