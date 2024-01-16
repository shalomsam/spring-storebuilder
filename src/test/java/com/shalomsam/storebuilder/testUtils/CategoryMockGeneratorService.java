package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.SeoMetaData;
import com.shalomsam.storebuilder.domain.shop.Category;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
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

    private final Faker faker = new Faker();

    private final Map<String, Category> cache = new HashMap<>();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    public List<Category> generateMock(int size) {
        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < size; i++) {
            String categoryName = faker.commerce().department();
            String categoryNameSlug = URLEncoder.encode(categoryName.toLowerCase(), StandardCharsets.UTF_8);
            Category category = null;
            if (!cache.containsKey(categoryName)) {
                category = Category.builder()
                    .id(new ObjectId().toString())
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
                    .parentCategories(null)
                    .build();

                category.setChildCategories(generateMockSubCategories(category, size));

                cache.put(categoryName, category);
            } else {
                category = cache.get(categoryName);
            }

            categories.add(category);
        }

        return new ArrayList<>(categories);
    }

    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        // Do nothing as we are already generating
        // ParentCategory > SubCategory > SubCategory relationships
    }

    public List<Category> generateMockSubCategories(Category parentCategory, int size) {
        Set<Category> subCategories = new HashSet<>();
        List<Category> parentCategoryList = new ArrayList<>();
        parentCategoryList.add(parentCategory);

        for (int i = 0; i < size; i++) {
            String subCategoryName = String.format("%s - SubCategory %d", parentCategory.getName(), i);
            String subCategoryNameSlug = URLEncoder.encode(subCategoryName.toLowerCase(), StandardCharsets.UTF_8);
            Category subCategory = Category.builder()
                .id(new ObjectId().toString())
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
                .parentCategories(parentCategoryList)
                .build();

            subCategory.setChildCategories(generateMockSubCategories(subCategory, size >= 4 ? size / 2 : size));
            subCategories.add(subCategory);
        }

        return new ArrayList<>(subCategories);
    }

}
