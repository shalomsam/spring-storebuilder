package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.Organization;
import com.shalomsam.storebuilder.model.user.ContactInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sellers")
public class Seller extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Transient
    private Organization organization;
    private String organizationId;

    private String name;

    private String shopSubDomain;

    private SellerType sellerType;

    private Boolean isOnline;

    private ContactInfo contactInfo;

    @Transient
    private List<Offer> offers;
}
