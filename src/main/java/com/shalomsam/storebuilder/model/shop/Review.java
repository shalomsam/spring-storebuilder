package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.user.Customer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class Review extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String sku;

    @Transient
    private ProductVariant productVariant;
    private String productVariantId;

    @Transient
    private Customer customer;
    private String customerId;

    private Float rating;

    private String title;

    private String description;
}
