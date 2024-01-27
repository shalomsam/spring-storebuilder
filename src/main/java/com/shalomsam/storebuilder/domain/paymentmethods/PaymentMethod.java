package com.shalomsam.storebuilder.domain.paymentmethods;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "paymentMethods")
public class PaymentMethod {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Transient
    private Customer customer;
    private String customerId;

    @Field("paymentMethodType")
    private String paymentMethodType;

    @Nullable
    private Card cardDetails;

    @Nullable
    @Field(targetType = FieldType.DECIMAL128)
    private BigInteger cashAmount;

    @Nullable
    private StripeDetails stripeDetails;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
