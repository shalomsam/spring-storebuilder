package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Category;
import com.shalomsam.storebuilder.model.shop.Product;
import com.shalomsam.storebuilder.model.shop.ProductVariant;
import com.shalomsam.storebuilder.repository.ProductRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ProductServiceImpl implements DomainService<Product> {

    private final ProductRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public ProductServiceImpl(ProductRepository productRepository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Flux<Product> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Product> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Product> create(Product entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Product> updateById(String id, Product partial) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (partial.getTitle() != null) update.set("title", partial.getTitle());
        if (partial.getDescription() != null) update.set("description", partial.getDescription());
        if (partial.getBrandName() != null) update.set("brandName", partial.getBrandName());
        if (partial.getModelName() != null) update.set("modelName", partial.getModelName());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Product.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    public Mono<Product> updateCategories(String productId, List<Category> categories, UpdateType updateType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Query query = Query.query(Criteria.where("_id").is(productId));
        Update update = new Update();

        // will invoke set, push or pop depending on the updateType value
        Method updateMethod = Update.class.getMethod(updateType.getDisplayName());
        updateMethod.invoke(update, categories);

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Product.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Product> updateMany(List<Product> entities) {
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

    private Mono<Product> zipWithRelatedEntities(Product product) {
        Query query = Query.query(Criteria.where("productId").is(product.getId()));
        Flux<ProductVariant> productVariantFlux = mongoTemplate.find(query, ProductVariant.class);

        return productVariantFlux.collectList().map(productVariants -> {
            product.setProductVariants(productVariants);
            return product;
        });
    }
}
