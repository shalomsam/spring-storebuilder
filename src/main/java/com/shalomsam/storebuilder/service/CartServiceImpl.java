package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.Cart;
import com.shalomsam.storebuilder.model.user.Customer;
import com.shalomsam.storebuilder.repository.CartRepository;
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
public class CartServiceImpl implements DomainService<Cart> {

    private  final CartRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public CartServiceImpl(CartRepository cartRepository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = cartRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Cart> getAll() {
        return repository.findAll().flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Cart> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Cart> create(Cart entity) {
        return repository.save(entity).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Cart> updateById(String id, Cart partialEntity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (partialEntity.getCartItems() != null) update.set("cartItems", partialEntity.getCartItems());
        if (partialEntity.getCartStatus() != null) update.set("cartStatus", partialEntity.getCartStatus());
        if (partialEntity.getCustomerId() != null) update.set("customerId", partialEntity.getCustomerId());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Cart.class
        ).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Cart> updateMany(List<Cart> entities) {
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

    private Mono<Cart> zipWithRelatedEntities(Cart cart) {
        Mono<Customer> customerMono = mongoTemplate.findById(cart.getCustomerId(), Customer.class);

        return customerMono.map(
            customer -> {
                cart.setCustomer(customer);
                return cart;
            }
        );
    }
}
