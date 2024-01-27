package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
public class Review {
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

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
