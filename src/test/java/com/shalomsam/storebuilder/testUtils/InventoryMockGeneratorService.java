package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.shop.Inventory;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.domain.shop.StockLocation;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class InventoryMockGeneratorService implements MockGeneratorService<Inventory> {

    static String COLLECTION_NAME = "inventories";

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return InventoryMockGeneratorService.COLLECTION_NAME;
    }

    @Override
    public List<Inventory> generateMock(int size) {
        List<Inventory> inventories = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Inventory inventory = Inventory.builder()
                .id(new ObjectId().toString())
//                .orders()
                .stockCount(faker.number().numberBetween(10, 2000))
                .location( (StockLocation) MockHelper.generateMockAddress())
                .auditMetadata(MockHelper.generateMockAuditMetadata())
//                .productVariant()
                .build();
            inventories.add(inventory);
        }

        return inventories;
    }

    @Override
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        List<Map> inventoryList = (List<Map>) entityMap.get(getCollectionName());
        List<Map> productVariants = (List<Map>) entityMap.get(ProductVariantMockGeneratorService.COLLECTION_NAME);
        List<Map> orders = (List<Map>) entityMap.get("orders");

        inventoryList = inventoryList.stream().peek(inventory -> {
            String mockRelatedVariantId = faker.options().nextElement(productVariants).get("id").toString();
            inventory.put("productVariantId", mockRelatedVariantId);

        }).toList();
    }
}
