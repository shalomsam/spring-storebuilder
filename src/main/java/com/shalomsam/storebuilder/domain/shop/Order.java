package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.*;
import com.shalomsam.storebuilder.domain.user.Address;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Order {
    @MongoId
    private ObjectId id;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    @Field("sellerId")
    @DocumentReference
    private Seller seller;

    private OrderStatus orderStatus;

    private List<CartItem> cartItems;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cartTotal;

    private List<PriceSummary> discounts;

    private List<PriceSummary> taxes;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal grandTotal;

    private List<Transaction> transactions;

    @Field("addressId")
    @DocumentReference
    private Address shippingAddress;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
