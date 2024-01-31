package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.model.user.Customer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private ObjectId id;

    @Transient
    private Customer customer;
    private String customerId;

    @Transient
    private Order order;
    private String orderId;

    @Transient
    private PaymentMethod paymentMethod;
    private String paymentMethodId;

    @Transient
    private Currency currency;
    private String currencyCode;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
}
