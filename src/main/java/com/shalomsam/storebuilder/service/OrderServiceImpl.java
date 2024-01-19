package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Order;
import com.shalomsam.storebuilder.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements DomainService<Order> {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Order> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Order> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Order> create(Order entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Order> updateById(String id, Order entity) {
        return repository.findById(id)
            .map(order -> {
                if (entity.getCustomer() != null) order.setCustomer(entity.getCustomer());
                if (entity.getSeller() != null) order.setSeller(entity.getSeller());
                if (entity.getOrderStatus() != null) order.setOrderStatus(entity.getOrderStatus());
                if (entity.getCartItems() != null) order.getCartItems().addAll(entity.getCartItems());
                if (entity.getCartTotal() != null) order.setCartTotal(entity.getCartTotal());
                if (entity.getTaxes() != null) order.setTaxes(entity.getTaxes());
                if (entity.getGrandTotal() != null) order.setGrandTotal(entity.getGrandTotal());
                if (entity.getTransactions() != null) order.getTransactions().addAll(entity.getTransactions());
                if (entity.getShippingAddress() != null) order.setShippingAddress(entity.getShippingAddress());
                if (entity.getInventory() != null) order.setInventory(entity.getInventory());

                return order;
            })
            .flatMap(repository::save);
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
}
