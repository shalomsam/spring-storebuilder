package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.model.shop.Category;
import com.shalomsam.storebuilder.model.shop.Product;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
public class ProductMockGeneratorService implements MockGeneratorService<Product> {

    static String COLLECTION_NAME = "products";

    @Autowired
    public ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Autowired
    private ProductVariantMockGenerator productVariantMockGenerator;

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }


    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public List<Product> generateMock(int size) {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String brand = faker.commerce().brand();
            String modelName = faker.commerce().productName();

            Product product = Product.builder()
                .id(new ObjectId().toString())
                .brandName(brand)
                .modelName(modelName)
                .title(String.format("%s - %s", brand, modelName))
                .description(faker.lorem().characters(200))
                .createdAt(MockHelper.generateMockAddress().getCreatedAt())
                //.auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            // generate mock variants for each mock product
            productVariantMockGenerator.generateMock(2, product);
            products.add(product);
        }

        return products;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Mono<List<Category>> categoriesMono = mongoTemplate.findAll(Category.class).collectList();
        Flux<Product> productFlux = mongoTemplate.findAll(Product.class);

        categoriesMono.flatMapMany(categories -> productFlux.map(product -> {
            Set<Category> randomCategories = faker.options().subset(1, categories.toArray(new Category[0]));
            List<String> categoryIds = randomCategories.stream().map(Category::getId).toList();
            product.setCategoryIds(categoryIds);
            return product;
        })
        .flatMap(mongoTemplate::save))
        .blockFirst();
    }
}
