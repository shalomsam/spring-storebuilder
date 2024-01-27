package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {
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

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
