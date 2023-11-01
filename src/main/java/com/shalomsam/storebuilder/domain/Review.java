package com.shalomsam.storebuilder.domain;

import com.shalomsam.storebuilder.domain.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Review {
    @MongoId
    private ObjectId id;

    private String sku;

    @Field("productId")
    @DocumentReference
    private Product product;

    @Field("customerId")
    @DocumentReference
    private Customer customer;

    private float rating;

    private String title;

    private String description;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
