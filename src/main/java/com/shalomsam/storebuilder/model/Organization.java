package com.shalomsam.storebuilder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.model.user.ContactInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "organizations")
public class Organization extends BaseDocument implements Serializable {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String name;

    private String shopUrl;

    private ContactInfo contactInfo;
}
