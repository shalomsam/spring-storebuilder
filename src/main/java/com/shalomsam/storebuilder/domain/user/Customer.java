package com.shalomsam.storebuilder.domain.user;

import com.shalomsam.storebuilder.domain.shop.Order;
import com.shalomsam.storebuilder.domain.shop.Review;
import com.shalomsam.storebuilder.domain.shop.Transaction;
import com.shalomsam.storebuilder.domain.paymentmethods.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer extends User {

    @Field("customerAccessId")
    @DocumentReference
    private CustomerAccess customerAccess;

    @Field("customerAddressIds")
    @DocumentReference
    private List<CustomerAddress> addresses;

    @Field("orderIds")
    @DocumentReference
    private List<Order> orders;

    @Field("paymentMethodIds")
    @DocumentReference
    private List<PaymentMethod> paymentMethods;

    @Field("reviewIds")
    @DocumentReference
    private List<Review> reviews;

    @Field("transactionIds")
    @DocumentReference
    private List<Transaction> transactions;

}
