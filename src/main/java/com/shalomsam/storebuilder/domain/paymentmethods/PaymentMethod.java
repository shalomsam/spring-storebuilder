package com.shalomsam.storebuilder.domain.paymentmethods;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {
    @MongoId
    private ObjectId id;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    private String nameOnCard;

    @Field("paymentMethodType")
    private String paymentMethodType;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
