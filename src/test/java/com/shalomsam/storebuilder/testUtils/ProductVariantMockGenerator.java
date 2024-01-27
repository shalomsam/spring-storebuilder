package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Product;
import com.shalomsam.storebuilder.domain.shop.ProductAttribute;
import com.shalomsam.storebuilder.domain.shop.ProductCondition;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class ProductVariantMockGenerator implements MockGeneratorService<ProductVariant> {

    static String COLLECTION_NAME = "productVariants";

    @Autowired
    public ObjectMapper objectMapper;

    @Getter
    private final Map<String, List<String>> productToVariantMap = new HashMap<>();

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
                //.product(product) <-- reason below
                .upc(String.valueOf(faker.barcode().ean8()))
                .attributes(generateMockAttributes())
                .listPrice(listPrice)
                .salePrice(salePrice)
                .bulkPrice(bulkPrice)
                .condition(faker.options().option(ProductCondition.class))
                .build();

            // can't link product and variant directly as Jackson will produce
            // infinite recursion. Hence, the linking will have to happen after
            // initial data load. So for now we'll store the mapping in memory
            String productId = product.getId();
            String variantId = productVariant.getId();
            if (!productToVariantMap.containsKey(productId)) {
                productToVariantMap.put(productId, Collections.singletonList(variantId));
            } else {
                // ArrayList in class property is immutable, hence need to create new ArrayList
                // before we can update its size.
                List<String> variantIds = new ArrayList<>(productToVariantMap.get(productId));
                variantIds.add(variantId);
                productToVariantMap.put(productId, variantIds);
            }

            // we don't set the sku yet since in the real world the sku is created
            // from the seller offer flow, ie when a productVariant offer is added
            // by the seller. So in the mock generator this responsibility is in
            // the OfferMockGeneratorService, after random offers have been linked
            // to sellers
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
    public void buildMockRelationShips(ApplicationContext applicationContext) {

    }
}
