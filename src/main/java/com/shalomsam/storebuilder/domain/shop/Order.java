package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.NonNull;
import com.shalomsam.storebuilder.domain.*;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private OrderStatus orderStatus;

    @Transient
    private Cart cart;
    private String cartId;

    @NonNull
    private String CurrencyCode = Currency.getInstance(Locale.CANADA).getCurrencyCode();

    private List<PriceSummary> discounts;

    private List<PriceSummary> taxes;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cartTotal;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal grandTotal;

    @Transient
    private Customer customer;
    private String customerId;

    @Transient
    private Seller seller;
    private String sellerId;

    @Transient
    private Inventory inventory;
    private String inventoryId;

    @Transient
    private ShippingDetails shippingDetails;
    private String shippingDetailsId;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
