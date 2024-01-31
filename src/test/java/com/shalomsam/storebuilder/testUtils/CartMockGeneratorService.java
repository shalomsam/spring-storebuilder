package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.model.shop.Cart;
import com.shalomsam.storebuilder.model.shop.CartItem;
import com.shalomsam.storebuilder.model.shop.CartStatus;
import com.shalomsam.storebuilder.model.shop.ProductVariant;
import com.shalomsam.storebuilder.model.user.Customer;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CartMockGeneratorService implements MockGeneratorService<Cart> {

    static String COLLECTION_NAME = "carts";

    @Autowired
    public ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return CartMockGeneratorService.COLLECTION_NAME;
    }


    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public List<Cart> generateMock(int size) {
        List<Cart> carts = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            carts.add(
                Cart.builder()
                    .id(new ObjectId().toString())
                    .cartStatus(faker.options().option(CartStatus.class))
                    //.auditMetadata(MockHelper.generateMockAuditMetadata())
                    .createdAt(MockHelper.generateMockAuditMetadata().getCreatedAt())
                    .build()
            );
        }

        return carts;
    }


    @Override
    public void buildMockRelationShips(ReactiveMongoTemplate mongoTemplate) {
        Mono<List<Customer>> customersMono = mongoTemplate.findAll(Customer.class).collectList();
        Mono<List<ProductVariant>> productVariantsMono = mongoTemplate.findAll(ProductVariant.class).collectList();
        Flux<Cart> cartFlux = mongoTemplate.findAll(Cart.class);

        Mono.zip(customersMono, productVariantsMono)
            .flatMapMany(tuple -> {
                List<Customer> customers = tuple.getT1();
                List<ProductVariant> productVariants = tuple.getT2();

                return cartFlux
                   .map(cart -> buildCartRelationShips(cart, customers, productVariants))
                   .flatMap(mongoTemplate::save);
            })
            .blockFirst();
    }

    private Cart buildCartRelationShips(Cart cart, List<Customer> customers, List<ProductVariant> productVariants) {
        Customer randomCustomer = faker.options().nextElement(customers);
        cart.setCustomerId(randomCustomer.getId());
        int cartCount = faker.number().numberBetween(1, 4);
        List<CartItem> cartItems = new ArrayList<>(cartCount);
        int quantity = faker.number().numberBetween(1, 10);

        for (int i = 0; i < cartCount; i++) {
            ProductVariant randomVariant = faker.options().nextElement(productVariants);
            CartItem item = CartItem.builder()
                .sku(randomVariant.getSku())
                .skuPrice(randomVariant.getListPrice())
                .quantity(quantity)
                .total(randomVariant.getListPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();

            cartItems.add(item);
        }

        cart.setCartItems(cartItems);
        return cart;
    }
}
