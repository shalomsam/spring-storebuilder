package com.shalomsam.storebuilder.service;

import com.shalomsam.storebuilder.domain.shop.Product;
import com.shalomsam.storebuilder.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductServiceImpl implements DomainService<Product> {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.repository = productRepository;
    }


    @Override
    public Flux<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Product> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Product> create(Product entity) {
        return repository.save(entity);
    }

    @Override
    public Mono<Product> updateById(String id, Product partial) {
        return repository.findById(id)
            .map(p -> {
                if (partial.getTitle() != null) p.setTitle(partial.getTitle());
                if (partial.getDescription() != null) p.setDescription(partial.getDescription());
                if (partial.getBrandName() != null) p.setBrandName(partial.getBrandName());
                if (partial.getModelName() != null) p.setModelName(partial.getModelName());
                if (partial.getCategories() != null) p.setCategories(partial.getCategories());
                if (partial.getProductVariants() != null) p.setProductVariants(partial.getProductVariants());

                return p;
            })
            .flatMap(repository::save);
    }

    @Override
    public Flux<Product> updateMany(List<Product> entities) {
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
