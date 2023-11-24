package com.shalomsam.storebuilder.domain;

import com.shalomsam.storebuilder.domain.user.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Organization implements Serializable {

    @MongoId
    private ObjectId id;

    private String name;

    private String shopUrl;

    private ContactInfo contactInfo;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
