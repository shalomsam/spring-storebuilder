package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.model.BaseDocument;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "discounts")
public class Discount extends BaseDocument {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @Transient
    private ProductVariant productVariant;
    private String productVariantId;

    private String title;

    private String description;

    @Nullable
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    private Integer percentage;

    @Field(targetType = FieldType.DATE_TIME)
    private ZonedDateTime startDateTime;

    @Field(targetType = FieldType.DATE_TIME)
    private ZonedDateTime endDateTime;
}
