package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.Organization;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sellers")
public class Seller {
    @MongoId
    private String id;

    @Field("organizationId")
    @DocumentReference
    private Organization organization;

    private String name;

    private String shopSubDomain;

    private SellerType sellerType;

    private Boolean isOnline;

    private ContactInfo contactInfo;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
