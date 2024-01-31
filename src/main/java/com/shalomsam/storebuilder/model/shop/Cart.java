package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.user.Customer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Transient
    private Customer customer;
    private String customerId;

    private CartStatus cartStatus;

    private List<CartItem> cartItems;
}
