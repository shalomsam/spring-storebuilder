package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.domain.*;
import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.domain.user.CustomerAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    private List<CartItem> cartItems;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cartTotal;

    private List<PriceSummary> discounts;

    private List<PriceSummary> taxes;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal grandTotal;

    private List<Transaction> transactions;

    private ShippingMethod shippingMethod;

    private LocalDate shippingDeadline;

    @Field("deliveryProviderId")
    private ShippingCarrier deliveryProvider;

    @Nullable
    private String shippingLabelImgUrl;

    @Nullable
    private String trackingCode;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    @Field("sellerId")
    @DocumentReference
    private Seller seller;

    @Field("addressId")
    @DocumentReference
    private CustomerAddress shippingAddress;

    @Field("inventoryId")
    @DocumentReference(lazy = true)
    private Inventory inventory;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
