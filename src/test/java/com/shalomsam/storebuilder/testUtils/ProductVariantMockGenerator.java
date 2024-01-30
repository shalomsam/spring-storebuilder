package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductVariantMockGenerator implements MockGeneratorService<ProductVariant> {

    static String COLLECTION_NAME = "productVariants";

    @Autowired
    public ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Getter
    @Setter
    List<ProductVariant> productVariants = new ArrayList<>();

    @Override
    public List<ProductVariant> generateMock(int size) {
        return productVariants;
    }

    public List<ProductVariant> generateMock(int size, Product product) {
        for (int i = 0; i < size; i++) {
            BigDecimal listPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, 5000));
            BigDecimal salePrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, listPrice.intValue()));
            BigDecimal bulkPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, listPrice.intValue()));
            ProductVariant productVariant = ProductVariant.builder()
                .id(new ObjectId().toString())
                .productId(product.getId())
                .upc(String.valueOf(faker.barcode().ean8()))
                .condition(faker.options().option(ProductCondition.class))
                .attributes(generateMockAttributes())
                .listPrice(listPrice)
                .salePrice(salePrice)
                .bulkPrice(bulkPrice)
                .build();

            productVariants.add(productVariant);
        }

        return productVariants;
    }


    public List<ProductAttribute> generateMockAttributes() {
        List<ProductAttribute> productAttributes = new ArrayList<>();

        productAttributes.add(
            ProductAttribute.builder()
                .name("color")
                .value(faker.expression("#{color.name}"))
                .group("feature")
                .build()
        );

        productAttributes.add(
            ProductAttribute.builder()
                .name("height")
                .value(faker.number().toString())
                .group("dimensions")
                .build()
        );

        productAttributes.add(
            ProductAttribute.builder()
                .name("width")
                .value(faker.number().toString())
                .group("dimensions")
                .build()
        );

        return productAttributes;
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Mono<List<Seller>> sellersMono = mongoTemplate.findAll(Seller.class).collectList();
        Flux<ProductVariant> variantFlux = mongoTemplate.findAll(ProductVariant.class);

        sellersMono.flatMapMany(sellers -> variantFlux.map(variant -> {
            Seller randomSeller = faker.options().nextElement(sellers);
            variant.setSellerId(randomSeller.getId());
            Mono<Product> product = mongoTemplate.findById(variant.getProductId(), Product.class);

            return product.zipWith(Mono.just(variant))
                .map(tuple -> {
                    Product product1 = tuple.getT1();
                    ProductVariant variant1 = tuple.getT2();
                    variant1.setSku(MockHelper.generateMockSku(variant1, product1, randomSeller));

                    return variant1;
                });
        })
        .flatMap(mongoTemplate::save))
        .blockFirst();
    }
}
