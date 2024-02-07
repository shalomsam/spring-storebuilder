package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.ProductVariant;
import com.shalomsam.storebuilder.model.shop.Review;
import com.shalomsam.storebuilder.model.user.Customer;
import com.shalomsam.storebuilder.repository.ReviewRepository;
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
public class ReviewServiceImpl implements DomainService<Review> {

    private final ReviewRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public ReviewServiceImpl(ReviewRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Review> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Review> getById(String id) {
        return repository.findById(id)
            .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Review> create(Review entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Review> updateById(String id, Review entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getSku() != null) update.set("sku", entity.getSku());
        if (entity.getProductVariantId() != null) update.set("productVariantId", entity.getProductVariantId());
        if (entity.getCustomerId() != null) update.set("customerId", entity.getCustomerId());
        if (entity.getRating() != null) update.set("rating", entity.getRating());
        if (entity.getTitle() != null) update.set("title", entity.getTitle());
        if (entity.getDescription() != null) update.set("description", entity.getDescription());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Review.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Review> updateMany(List<Review> entities) {
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

    private Mono<Review> zipWithRelatedEntities(Review review) {
        Mono<ProductVariant> productVariantMono = mongoTemplate.findById(review.getProductVariantId(), ProductVariant.class);
        Mono<Customer> customerMono = mongoTemplate.findById(review.getCustomerId(), Customer.class);

        return Mono.zip(productVariantMono, customerMono, (p, c) -> {
            review.setProductVariant(p);
            review.setCustomer(c);
            return review;
        });
    }
}
