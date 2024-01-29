package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.SeoMetaData;
import com.shalomsam.storebuilder.domain.shop.Category;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class CategoryMockGeneratorService implements MockGeneratorService<Category> {

    static String COLLECTION_NAME = "categories";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private final Faker faker = new Faker();

    private final Map<String, Category> cache = new HashMap<>();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }


    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate reactiveMongoTemplate) {
        // Do nothing as we are already generating
        // ParentCategory > SubCategory > SubCategory relationships
    }

    public List<Category> generateMock(int size) {
        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < size; i++) {
            String categoryName = faker.commerce().department();
            String categoryNameSlug = URLEncoder.encode(categoryName.toLowerCase(), StandardCharsets.UTF_8);
            String categoryId = new ObjectId().toString();

            Category category = Category.builder()
                .id(categoryId)
                .name(categoryName.toUpperCase())
                .seoMetaData(
                    SeoMetaData.builder()
                        .title(String.format("meta title for %s", categoryName))
                        .description(String.format("meta description for %s", categoryName))
                        .keywords(String.format("keywords for %s", categoryName))
                        .build()
                )
                .imageUrl(String.format("http:/test.com/images/%s.png", categoryNameSlug))
                .nameSlug(categoryNameSlug)
                .isActive(true)
                .parentCategoryIds(null)
                .childCategoryIds(generateMockSubCategories(categoryId, size/2))
                .build();

            categories.add(category);
        }

        return new ArrayList<>(categories);
    }

    public Set<String> generateMockSubCategories(String parentCategoryId, int size) {
        Set<String> subCategoriesIds = new HashSet<>();
        Set<String> parenCategoryIds = new HashSet<>();
        parenCategoryIds.add(parentCategoryId);

        for (int i = 0; i < size; i++) {
            String subCategoryName = String.format("%s - SubCategory %d", parentCategoryId, i);
            String subCategoryNameSlug = URLEncoder.encode(subCategoryName.toLowerCase(), StandardCharsets.UTF_8);
            String subCategoryId = new ObjectId().toString();

            Category subCategory = Category.builder()
                .id(subCategoryId)
                .name(subCategoryName.toUpperCase())
                .seoMetaData(
                    SeoMetaData.builder()
                        .title(String.format("meta title for %s", subCategoryName))
                        .description(String.format("meta description for %s", subCategoryName))
                        .keywords(String.format("keywords for %s", subCategoryName))
                        .build()
                )
                .imageUrl(String.format("http:/test.com/images/%s.png", subCategoryNameSlug))
                .nameSlug(subCategoryNameSlug)
                .isActive(true)
                .parentCategoryIds(parenCategoryIds)
                .build();

//            if (size > 2) {
//                subCategory.setChildCategories(generateMockSubCategories(subCategory, 1));
//            }

            // save children/subCategory first
            mongoTemplate.save(subCategory).block();
            subCategoriesIds.add(subCategoryId);
        }

        // return subCategoriesIds to be added to ParentCategory
        return subCategoriesIds;
    }

}
