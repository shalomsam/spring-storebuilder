package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.model.shop.*;
import com.shalomsam.storebuilder.model.user.Customer;
import com.shalomsam.storebuilder.repository.OrderRepository;
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
public class OrderServiceImpl implements DomainService<Order> {

    private final OrderRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    public OrderServiceImpl(OrderRepository repository, ReactiveMongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Order> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Order> getById(String id) {
        return repository.findById(id).flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Mono<Order> create(Order entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Order> updateById(String id, Order entity) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();

        if (entity.getCustomerId() != null) update.set("customerId", entity.getCustomerId());
        if (entity.getSellerId() != null) update.set("sellerId", entity.getSellerId());
        if (entity.getOrderStatus() != null) update.set("orderStatus", entity.getOrderStatus());
        if (entity.getCartId() != null) update.set("cartId", entity.getCartId());
        if (entity.getCartTotal() != null) update.set("cartTotal", entity.getCartTotal());
        // enable add / modify for lists?
        if (entity.getDiscounts() != null) update.set("discounts", entity.getDiscounts());
        if (entity.getTaxes() != null) update.set("taxes", entity.getTaxes());
        if (entity.getGrandTotal() != null) update.set("grandTotal", entity.getGrandTotal());
        if (entity.getCustomerId() != null) update.set("customerId", entity.getCustomerId());
        if (entity.getSellerId() != null) update.set("sellerId", entity.getSellerId());
        if (entity.getInventoryId() != null) update.set("inventoryId", entity.getInventoryId());
        if (entity.getShippingDetailsId() != null) update.set("shippingDetailsId", entity.getShippingDetailsId());
//        if (entity.getShippingMethod() != null) update.set("shippingMethod", entity.getShippingMethod());
//        if (entity.getShippingDeadline() != null) update.set("shippingDeadline", entity.getShippingDeadline());
//        if (entity.getShippingCarrierId() != null) update.set("shippingCarrierId", entity.getShippingCarrierId());
//        if (entity.getShippingLabelImgUrl() != null) update.set("shippingLabelImgUrl", entity.getShippingLabelImgUrl());
//        if (entity.getShippingAddressId() != null) update.set("shippingAddressId", entity.getShippingAddressId());

        return mongoTemplate.findAndModify(
            query,
            update,
            new FindAndModifyOptions().returnNew(true),
            Order.class
        )
        .flatMap(this::zipWithRelatedEntities);
    }

    @Override
    public Flux<Order> updateMany(List<Order> entities) {
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

    private Mono<Order> zipWithRelatedEntities(Order order) {
        Mono<Cart> cartMono = mongoTemplate.findById(order.getCartId(), Cart.class);
        Mono<Customer> customerMono = mongoTemplate.findById(order.getCustomerId(), Customer.class);
        Mono<Seller> sellerMono = mongoTemplate.findById(order.getSellerId(), Seller.class);
        Mono<Inventory> inventoryMono = mongoTemplate.findById(order.getInventoryId(), Inventory.class);
        Mono<ShippingDetails> shippingDetailsMono = mongoTemplate.findById(order.getShippingDetailsId(), ShippingDetails.class);

        return Mono.zip(cartMono, customerMono, sellerMono, inventoryMono, shippingDetailsMono)
            .map(tuple -> {
                order.setCart(tuple.getT1());
                order.setCustomer(tuple.getT2());
                order.setSeller(tuple.getT3());
                order.setInventory(tuple.getT4());
                order.setShippingDetails(tuple.getT5());

                return order;
            });
    }
}
