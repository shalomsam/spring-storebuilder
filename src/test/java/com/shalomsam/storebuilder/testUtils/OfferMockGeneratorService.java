package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Inventory;
import com.shalomsam.storebuilder.domain.shop.Offer;
import com.shalomsam.storebuilder.domain.shop.OfferStatus;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.domain.shop.Seller;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferMockGeneratorService implements MockGeneratorService<Offer> {

    public static String COLLECTION_NAME = "offers";

    @Autowired
    private ObjectMapper objectMapper;

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
    public List<Offer> generateMock(int size) {
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Offer offer = Offer.builder()
                .id(new ObjectId().toString())
                .offerStatus(faker.options().option(OfferStatus.class))
                .auditMetadata(MockHelper.generateMockAuditMetadata())
                .build();

            offers.add(offer);
        }

        return offers;
    }


    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Flux<Offer> offerFlux = mongoTemplate.findAll(Offer.class);
        Mono<List<ProductVariant>> variantsMono = mongoTemplate.findAll(ProductVariant.class).collectList();
        Mono<List<Seller>> sellersMono = mongoTemplate.findAll(Seller.class).collectList();
        Mono<List<Inventory>> inventoryMono = mongoTemplate.findAll(Inventory.class).collectList();

        Mono.zip(variantsMono, sellersMono, inventoryMono)
            .flatMapMany(tuple -> offerFlux.map(offer -> {
                ProductVariant randomVariant = faker.options().nextElement(tuple.getT1());
                Seller randomSeller = faker.options().nextElement(tuple.getT2());
                Inventory randomInventory = faker.options().nextElement(tuple.getT3());

                offer.setProductVariantId(randomVariant.getId());
                offer.setSellerId(randomSeller.getId());
                offer.setInventoryId(randomInventory.getId());
                return offer;
            })
            .flatMap(mongoTemplate::save))
            .blockFirst();
    }
}
