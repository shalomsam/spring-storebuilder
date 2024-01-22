package com.shalomsam.storebuilder.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shalomsam.storebuilder.domain.shop.Cart;
import com.shalomsam.storebuilder.domain.shop.CartItem;
import com.shalomsam.storebuilder.domain.shop.CartStatus;
import com.shalomsam.storebuilder.domain.shop.ProductVariant;
import com.shalomsam.storebuilder.domain.user.Customer;
import com.shalomsam.storebuilder.repository.CartRepository;
import com.shalomsam.storebuilder.repository.CustomerRepository;
import com.shalomsam.storebuilder.repository.ProductVariantRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Service
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
                    .auditMetadata(MockHelper.generateMockAuditMetadata())
                    .build()
            );
        }

        return carts;
    }


    @Override
    public void buildMockRelationShips(ApplicationContext applicationContext) {
        CartRepository cartRepository = applicationContext.getBean(CartRepository.class);
        CustomerRepository customerRepository = applicationContext.getBean(CustomerRepository.class);
        List<Customer> customerList = customerRepository.findAll().toStream().toList();

        ProductVariantRepository variantRepository = applicationContext.getBean(ProductVariantRepository.class);
        List<ProductVariant> productVariants = variantRepository.findAll().toStream().toList();

        Flux<Cart> cartFlux = cartRepository.findAll();

        cartFlux.map(cart -> {
            Customer randomCustomer = faker.options().nextElement(customerList);
            cart.setCustomer(randomCustomer);

            int cartCount = faker.number().numberBetween(1, 4);
            List<CartItem> cartItems = new ArrayList<>(cartCount);
            ProductVariant randomVariant = faker.options().nextElement(productVariants);
            int quantity = faker.number().numberBetween(1, 10);

            for (int i = 0; i < cartCount; i++) {
                cartItems.add(
                    CartItem.builder()
                        .sku(randomVariant.getSku())
                        .skuPrice(randomVariant.getListPrice())
                        .quantity(quantity)
                        .total(randomVariant.getListPrice().multiply(BigDecimal.valueOf(quantity)))
                        .build()
                );
            }

            cart.setCartItems(cartItems);

            return cart;
        })
        .flatMap(cartRepository::save);
    }


}
