package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.*;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductVariantServiceImpl implements DomainService<ProductVariant> {

    private final ProductVariantRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public ProductVariantServiceImpl(ProductVariantRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<ProductVariant> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<ProductVariant> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<ProductVariant> create(ProductVariant entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<ProductVariant> updateById(String id, ProductVariant entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getProductId() != null) update.set("productId", entity.getProductId());
        if (entity.getSku() != null) update.set("sku", entity.getSku());
        if (entity.getUpc() != null) update.set("upc", entity.getUpc());
        if (entity.getSellerId() != null) update.set("sellerId", entity.getSellerId());
        if (entity.getCondition() != null) update.set("condition", entity.getCondition());
        if (entity.getAttributes() != null) update.set("attributes", entity.getAttributes());
        if (entity.getCurrencyCode() != null) update.set("currencyCode", entity.getCurrencyCode());
        if (entity.getListPrice() != null) update.set("listPrice", entity.getListPrice());
        if (entity.getSalePrice() != null) update.set("salePrice", entity.getSalePrice());
        if (entity.getBulkPrice() != null) update.set("bulkPrice", entity.getBulkPrice());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            ProductVariant.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<ProductVariant> updateMany(List<ProductVariant> entities) {
        return repository.saveAll(entities);
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

    private Mono<ProductVariant> zipWithRelatedEntities(ProductVariant productVariant) {
        Query query = Query.query(Criteria.where("productVariantId").is(productVariant.getId()));

        Flux<Inventory> inventoryFlux = mongoTemplate.find(query, Inventory.class);
        Flux<Discount> discountFlux = mongoTemplate.find(query, Discount.class);
        Flux<Offer> offerFlux = mongoTemplate.find(query, Offer.class);
        Mono<Product> productMono = mongoTemplate.findById(productVariant.getProductId(), Product.class);

        return Mono.zip(productMono, inventoryFlux.collectList(), discountFlux.collectList(), offerFlux.collectList())
            .map(tuple -> {
                productVariant.setProduct(tuple.getT1());
                productVariant.setInventoryList(tuple.getT2());
                productVariant.setDiscounts(tuple.getT3());
                productVariant.setOffers(tuple.getT4());
                return productVariant;
            });

    }
}
