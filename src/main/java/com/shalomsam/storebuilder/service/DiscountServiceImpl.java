package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Discount;
import com.shalomsam.storebuilder.model.shop.ProductVariant;
import com.shalomsam.storebuilder.repository.DiscountRepository;
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
public class DiscountServiceImpl implements DomainService<Discount> {

    private final DiscountRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public DiscountServiceImpl(DiscountRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Discount> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Discount> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Discount> create(Discount entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Discount> updateById(String id, Discount entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getProductVariantId() != null) update.set("productVariantId", entity.getProductVariantId());
        if (entity.getTitle() != null) update.set("title", entity.getTitle());
        if (entity.getDescription() != null) update.set("description", entity.getDescription());
        if (entity.getAmount() != null) update.set("amount", entity.getAmount());
        if (entity.getPercentage() != null) update.set("percentage", entity.getPercentage());
        if (entity.getStartDateTime() != null) update.set("startDateTime", entity.getStartDateTime());
        if (entity.getEndDateTime() != null) update.set("endDateTime", entity.getEndDateTime());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Discount.class
        ).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Discount> updateMany(List<Discount> entities) {
        return repository.saveAll(entities).flatMap(this::zipWithRelatedEntities);
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

    private Mono<Discount> zipWithRelatedEntities(Discount discount) {
        Mono<ProductVariant> productVariantMono = mongoTemplate.findById(discount.getProductVariantId(), ProductVariant.class);

        return productVariantMono.map(productVariant -> {
           discount.setProductVariant(productVariant);
           return discount;
        });
    }
}
