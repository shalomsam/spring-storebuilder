package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductVariantServiceImpl implements DomainService<ProductVariant> {

    private final ProductVariantRepository repository;

    public ProductVariantServiceImpl(ProductVariantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<ProductVariant> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<ProductVariant> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<ProductVariant> create(ProductVariant entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<ProductVariant> updateById(String id, ProductVariant entity) {
        return repository.findById(id)
            .map(productVariant -> {
                if (entity.getProduct() != null) productVariant.setProduct(entity.getProduct());
                if (entity.getSku() != null) productVariant.setSku(entity.getSku());
                if (entity.getUpc() != null) productVariant.setUpc(entity.getUpc());
                if (entity.getSeller() != null) productVariant.setSeller(entity.getSeller());
                if (entity.getCondition() != null) productVariant.setCondition(entity.getCondition());
                if (entity.getAttributes() != null) productVariant.getAttributes().addAll(entity.getAttributes());
                if (entity.getListPrice() != null) productVariant.setListPrice(entity.getListPrice());
                if (entity.getSalePrice() != null) productVariant.setSalePrice(entity.getSalePrice());
                if (entity.getBulkPrice() != null) productVariant.setBulkPrice(entity.getBulkPrice());
                if (entity.getInventoryList() != null) productVariant.getInventoryList().addAll(entity.getInventoryList());
                if (entity.getDiscounts() != null) productVariant.getDiscounts().addAll(entity.getDiscounts());

                return productVariant;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<ProductVariant> updateMany(List<ProductVariant> entities) {
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
