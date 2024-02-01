package com.shalomsam.storebuilder.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PersistableAddress extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String unit;

    private String buildingNumber;

    private String street;

    private String city;

    private String state;

    private String country;

    private String postalCode;
}

