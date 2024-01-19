package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Cart;
import com.shalomsam.storebuilder.repository.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CartServiceImpl implements DomainService<Cart> {

    private  final CartRepository repository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.repository = cartRepository;
    }

    @Override
    public Flux<Cart> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Cart> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Cart> create(Cart entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Cart> updateById(String id, Cart partialEntity) {
        return repository.findById(id)
            .map(cart -> {

                if (partialEntity.getCartItems() != null) cart.setCartItems(partialEntity.getCartItems());
                if (partialEntity.getCartStatus() != null) cart.setCartStatus(partialEntity.getCartStatus());
                if (partialEntity.getCustomer() != null) cart.setCustomer(partialEntity.getCustomer());

                return cart;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Cart> updateMany(List<Cart> entities) {
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
