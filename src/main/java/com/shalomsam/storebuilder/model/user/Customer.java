package com.shalomsam.storebuilder.model.user;

import com.shalomsam.storebuilder.model.shop.Order;
import com.shalomsam.storebuilder.model.shop.Review;
import com.shalomsam.storebuilder.model.shop.Transaction;
import com.shalomsam.storebuilder.model.paymentmethods.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer extends User {

    @Transient
    private CustomerAccess customerAccess;
    private String customerAccessId;

    @Transient
    private List<CustomerAddress> addresses;

    @Transient
    private List<Order> orders;

    @Transient
    private List<PaymentMethod> paymentMethods;

    @Transient
    private List<Review> reviews;

    @Transient
    private List<Transaction> transactions;

}
