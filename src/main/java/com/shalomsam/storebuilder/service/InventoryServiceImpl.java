package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Inventory;
import com.shalomsam.storebuilder.model.shop.ProductVariant;
import com.shalomsam.storebuilder.model.shop.StockLocation;
import com.shalomsam.storebuilder.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class InventoryServiceImpl implements DomainService<Inventory> {

    private final InventoryRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public InventoryServiceImpl(InventoryRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Inventory> getAll() {
        return repository.findAll()
            .flatMap(this::zipWithRelatedModels);
    }

    @Override
    public Mono<Inventory> getById(String id) {
        return repository.findById(id)
            .flatMap(this::zipWithRelatedModels);
    }

    @Override
    public Mono<Inventory> create(Inventory entity) {
        return repository.save(entity)
            .flatMap(this::zipWithRelatedModels);
    }

    @Override
    public Mono<Inventory> updateById(String id, Inventory entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getProductVariantId() != null) update.set("productVariantId", entity.getProductVariantId());
        if (entity.getStockLocationId() != null) update.set("stockLocationId", entity.getStockLocationId());
        if (entity.getStockCount() != null) update.set("stockCount", entity.getStockCount());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Inventory.class
        )
        .flatMap(this::zipWithRelatedModels);
    }

    @Override
    public Flux<Inventory> updateMany(List<Inventory> entities) {
        return repository.saveAll(entities).flatMap(this::zipWithRelatedModels);
    }

    @Override
    public Mono<Integer> deleteById(String id) {
        return repository.deleteById(id).thenReturn(1);
    }

    @Override
    public Mono<Integer> deleteManyById(List<String> ids) {
        return repository.deleteAllByIdIn(ids);
    }

    @Override
    public Mono<Long> getCount() {
        return repository.count();
    }


    private Mono<Inventory> zipWithRelatedModels(Inventory inventory) {
        Mono<ProductVariant> productVariantMono = mongoTemplate.findById(inventory.getProductVariantId(), ProductVariant.class);
        Mono<StockLocation> stockLocationMono = mongoTemplate.findById(inventory.getStockLocationId(), StockLocation.class);

        return Mono.zip(productVariantMono, stockLocationMono, (p, s) -> {
            inventory.setProductVariant(p);
            inventory.setStockLocation(s);
            return inventory;
        });
    }
}
