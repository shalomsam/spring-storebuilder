package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.Organization;
import com.shalomsam.storebuilder.repository.OrganizationRepository;
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
public class OrganizationServiceImpl implements DomainService<Organization> {

    private final OrganizationRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public OrganizationServiceImpl(OrganizationRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Organization> getAll() {
        // TODO: implement pageable defaults
        return repository.findAll();
    }

    @Override
    public Mono<Organization> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Organization> create(Organization organization) {
        return repository.save(organization);
    }

    @Override
    public Mono<Organization> updateById(String id, Organization entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getName() != null) update.set("name", entity.getName());
        if (entity.getShopUrl() != null) update.set("shopUrl", entity.getShopUrl());
        if (entity.getContactInfo() != null) update.set("contactInfo", entity.getContactInfo());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Organization.class
        );
    }

    @Override
    public Flux<Organization> updateMany(List<Organization> orgDTOs) {
        return repository.saveAll(orgDTOs);
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

    //TODO: Add bulk update method
}
