package com.shalomsam.storebuilder.domain.shop;

import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Discount {

    @MongoId
    private ObjectId id;

    @Field("productVariantId")
    @DocumentReference
    private ProductVariant productVariant;

    private String title;

    private String description;

    @Nullable
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    private int percentage;

    @Field(targetType = FieldType.DATE_TIME)
    private ZonedDateTime startDateTime;

    @Field(targetType = FieldType.DATE_TIME)
    private ZonedDateTime endDateTime;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;

}
