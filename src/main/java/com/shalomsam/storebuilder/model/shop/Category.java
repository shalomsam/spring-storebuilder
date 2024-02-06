package com.shalomsam.storebuilder.model.shop;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.shalomsam.storebuilder.model.BaseDocument;
import com.shalomsam.storebuilder.model.SeoMetaData;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Category extends BaseDocument {
    @Id
    @MongoId
    @Field(name = "_id")
    @JsonProperty("_id")
    private String id;

    private String name;

    private String nameSlug;

    @Builder.Default
    private Boolean isActive = false;

    private String imageUrl;

    private SeoMetaData seoMetaData;

    @Nullable
    private Set<String> parentCategoryIds;

    @Nullable
    private Set<String> childCategoryIds;
}
