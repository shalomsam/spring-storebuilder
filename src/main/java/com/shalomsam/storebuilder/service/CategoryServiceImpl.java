package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Category;
import com.shalomsam.storebuilder.repository.CategoryRepository;
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
public class CategoryServiceImpl implements DomainService<Category> {

    private final CategoryRepository repository;

    private  final ReactiveMongoTemplate mongoTemplate;

    public CategoryServiceImpl(CategoryRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Category> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Category> create(Category entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Category> updateById(String id, Category entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getName() != null) update.set("name", entity.getName());
        if (entity.getNameSlug() != null) update.set("nameSlug", entity.getNameSlug());
        if (entity.getIsActive() != null) update.set("isActive", entity.getIsActive());
        if (entity.getImageUrl() != null) update.set("imageUrl", entity.getImageUrl());
        if (entity.getSeoMetaData() != null) update.set("seoMetaData", entity.getSeoMetaData());
        if (entity.getParentCategoryIds() != null) update.set("parentCategoryIds", entity.getParentCategoryIds());
        if (entity.getChildCategoryIds() != null) update.set("childCategoryIds", entity.getChildCategoryIds());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Category.class
        );
    }

    @Override
    public Flux<Category> updateMany(List<Category> entities) {
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
}
