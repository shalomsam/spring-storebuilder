package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sellers")
public class Seller {
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

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
