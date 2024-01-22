package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Discount;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.repository.DiscountRepository;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    @Autowired
    public ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }


    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
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
    public void buildMockRelationShips(ApplicationContext applicationContext) {
        DiscountRepository discountRepository = applicationContext.getBean(DiscountRepository.class);
        ProductVariantRepository variantRepository = applicationContext.getBean(ProductVariantRepository.class);

        List<ProductVariant> variantList = variantRepository.findAll().toStream().toList();
        Flux<Discount> discountFlux = discountRepository.findAll();

        discountFlux.map(discount -> {
            ProductVariant randomVariant = faker.options().nextElement(variantList);
            String randomVariantId = randomVariant.getId();
            discount.setProductVariant(randomVariant);

            variantRepository.findById(randomVariantId)
                .map(productVariant -> {
                    productVariant.getDiscounts().add(discount);
                    return productVariant;
                })
                .flatMap(variantRepository::save);

            return discount;
        })
        .flatMap(discountRepository::save);
    }
}
