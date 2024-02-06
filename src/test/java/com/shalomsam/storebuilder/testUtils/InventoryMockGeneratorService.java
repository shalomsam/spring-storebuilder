package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.model.shop.Inventory;
import com.shalomsam.storebuilder.model.shop.ProductVariant;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InventoryMockGeneratorService implements MockGeneratorService<Inventory> {

    static String COLLECTION_NAME = "inventories";

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private StockLocationGeneratorService stockLocationGeneratorService;

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return InventoryMockGeneratorService.COLLECTION_NAME;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public List<Inventory> generateMock(int size) {
        List<Inventory> inventories = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Inventory inventory = Inventory.builder()
                .id(new ObjectId().toString())
                .stockCount(faker.number().numberBetween(10, 2000))
                .createdAt(MockHelper.generateMockAuditMetadata().getCreatedAt())
                .build();

            // supports only one location to an inventory listing
            inventory.setStockLocationId(
                stockLocationGeneratorService.generateMock(inventory).getId()
            );

            inventories.add(inventory);
        }

        return inventories;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Mono<List<ProductVariant>> productVariantsMono = mongoTemplate.findAll(ProductVariant.class).collectList();
        Flux<Inventory> inventoryFlux = mongoTemplate.findAll(Inventory.class);

        inventoryFlux.flatMap(inventory -> productVariantsMono.flatMapMany(productVariants -> {
            ProductVariant randomVariant = faker.options().nextElement(productVariants);
            inventory.setProductVariantId(randomVariant.getId());
            return Mono.just(inventory);
        })
        .flatMap(mongoTemplate::save))
        .collectList()
        .block();
    }
}
