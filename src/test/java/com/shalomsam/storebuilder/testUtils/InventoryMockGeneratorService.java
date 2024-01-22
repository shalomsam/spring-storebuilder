package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Inventory;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InventoryMockGeneratorService implements MockGeneratorService<Inventory> {

    static String COLLECTION_NAME = "inventories";

    @Autowired
    public ObjectMapper objectMapper;

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
                .location(MockHelper.generateMockStockLocation())
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();
            inventories.add(inventory);
        }

        return inventories;
    }

    @Override
    public void buildMockRelationShips(ApplicationContext applicationContext) {
        
    }
}
