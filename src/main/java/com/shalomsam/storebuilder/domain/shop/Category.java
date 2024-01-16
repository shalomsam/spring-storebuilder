package com.shalomsam.storebuilder.domain.shop;

import com.shalomsam.storebuilder.domain.AuditMetadata;
import com.shalomsam.storebuilder.domain.SeoMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Category {
    @MongoId
    private String id;

    private String name;

    private String nameSlug;

    private boolean isActive = false;

    private String imageUrl;

    private SeoMetaData seoMetaData;

    @Field("parentCategoryIds")
    @DocumentReference
    private List<Category> parentCategories;

    @Field("childCategoryIds")
    @DocumentReference
    private List<Category> childCategories;

    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    private AuditMetadata auditMetadata;
}
