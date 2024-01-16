package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.shop.Discount;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DiscountMockGeneratorService implements MockGeneratorService<Discount> {

    static String COLLECTION_NAME = "discounts";

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<Discount> generateMock(int size) {
        List<Discount> discounts = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Discount discount = Discount.builder()
                .id(new ObjectId().toString())
                .title("discount: " + faker.commerce().promotionCode())
                .description(faker.lorem().sentence())
                .startDateTime(ZonedDateTime.of(faker.date().past(1, TimeUnit.DAYS).toLocalDateTime(), ZoneId.systemDefault()))
                .endDateTime(ZonedDateTime.of(faker.date().future(1, TimeUnit.DAYS).toLocalDateTime(), ZoneId.systemDefault()))
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            if (faker.bool().bool()) {
                discount.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 100)));
            } else {
                discount.setPercentage(faker.random().nextInt(10, 50));
            }

            discounts.add(discount);
        }

        return discounts;
    }

    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        List<Map> discounts = (List<Map>) entityMap.get(COLLECTION_NAME);
        List<Map> productVariants = (List<Map>) entityMap.get("productVariant");

        discounts = discounts.stream().peek(discount -> {
            Map relatedVariant = faker.options().nextElement(productVariants);
            discount.put("productVariantId", relatedVariant.get("id"));
        }).toList();

        entityMap.put(getCollectionName(), discounts);
    }
}
