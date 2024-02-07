package com.shalomsam.storebuilder.model.paymentmethods;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "paymentMethods")
public class PaymentMethod extends BaseDocument {
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
}
