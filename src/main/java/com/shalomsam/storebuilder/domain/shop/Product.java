package com.shalomsam.storebuilder.domain.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shalomsam.storebuilder.domain.AuditMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private String brandName;

    @Indexed
    private String modelName;

    // root categories
    @Transient
    private List<Category> categories;
    @Indexed
    private List<String> categoryIds;

    @Transient
    private List<ProductVariant> productVariants;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;

}
