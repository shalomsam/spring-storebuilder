package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.user.Address;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shippingDetails")
public class ShippingDetails extends BaseDocument {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private ShippingMethod shippingMethod;

    private LocalDate shippingDeadline;

    @Transient
    private ShippingCarrier shippingCarrier;
    private String shippingCarrierId;

    @Nullable
    private String shippingLabelImgUrl;

    @Nullable
    private String trackingCode;

    private Address address;
}
