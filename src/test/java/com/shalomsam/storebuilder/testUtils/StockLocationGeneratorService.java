package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.model.shop.Inventory;
import com.shalomsam.storebuilder.model.shop.StockLocation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockLocationGeneratorService implements MockGeneratorService<StockLocation> {

    static String COLLECTION_NAME = "stockLocations";

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public ReactiveMongoTemplate mongoTemplate;

    @Getter
    @Setter
    List<StockLocation> stockLocations = new ArrayList<>();

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public List<StockLocation> generateMock(int size) {
        return stockLocations;
    }

    public StockLocation generateMock(Inventory inventory) {
        StockLocation location = MockHelper.generateMockStockLocation();
        stockLocations.add(location);
        return location;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate reactiveMongoTemplate) {

    }
}
