package com.shalomsam.storebuilder.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.shop.Seller;
import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "organizations")
public class Organization implements Serializable {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String name;

    private String shopUrl;

    private ContactInfo contactInfo;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
