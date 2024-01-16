package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.shop.ProductAttribute;
import com.shalomsam.storebuilder.domain.shop.ProductCondition;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductVariantMockGeneratorService implements MockGeneratorService<ProductVariant> {

    static String COLLECTION_NAME = "productVariants";

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<ProductVariant> generateMock(int size) {
        List<ProductVariant> productVariants = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            BigDecimal listPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, 5000));
            BigDecimal salePrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, listPrice.intValue()));
            BigDecimal bulkPrice = BigDecimal.valueOf(faker.number().randomDouble(2, 50, listPrice.intValue()));
            ProductVariant productVariant = ProductVariant.builder()
                .id(new ObjectId().toString())
                .upc(String.valueOf(faker.barcode().ean8()))
                .attributes(generateMockAttributes())
                .listPrice(listPrice)
                .salePrice(salePrice)
                .bulkPrice(bulkPrice)
                .condition(faker.options().option(ProductCondition.class))
                .build();

            productVariant.setSku(generateMockSku(productVariant));
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

    public String generateMockSku(ProductVariant productVariant) {
        return String.format(
            "%.5s-%.5s-%.5s",
            productVariant.getProduct().getBrandName().toUpperCase(),
            productVariant.getAttributes().toString().replaceAll(" ", ""),
            productVariant.getSeller().getId()
        );
    }

    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        List<Map> productVariants = (List<Map>) entityMap.get(COLLECTION_NAME);
        List<Map> sellers = (List<Map>) entityMap.get("sellers");
        List<Map> inventories = (List<Map>) entityMap.get(InventoryMockGeneratorService.COLLECTION_NAME);
        List<Map> discounts = (List<Map>) entityMap.get(DiscountMockGeneratorService.COLLECTION_NAME);

        // to include in random mock options
        // some variants may not have a discount
        discounts.add(null);

        productVariants = productVariants.stream().peek(variant -> {
            variant.put("sellerId", faker.options().nextElement(sellers));
            variant.put("inventoryIds", faker.options().nextElement(inventories));
            variant.put("discountIds", faker.options().nextElement(discounts));
        }).toList();

        entityMap.put(getCollectionName(), productVariants);
    }
}
