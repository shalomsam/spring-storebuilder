package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Offer;
import com.shalomsam.storebuilder.repository.OfferRepository;
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
public class OfferServiceImpl implements DomainService<Offer> {

    private final OfferRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public OfferServiceImpl(OfferRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Offer> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Offer> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Offer> create(Offer entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Offer> updateById(String id, Offer entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getOfferStatus() != null) update.set("offerStatus", entity.getOfferStatus());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Offer.class
        );
    }

    @Override
    public Flux<Offer> updateMany(List<Offer> entities) {
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
