package com.shalomsam.storebuilder.testUtils;

import com.shalomsam.storebuilder.domain.shop.Cart;
import com.shalomsam.storebuilder.domain.shop.CartStatus;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CartMockGeneratorService implements MockGeneratorService<Cart> {

    static String COLLECTION_NAME = "carts";

    private final Faker faker = new Faker();

    @Override
    public String getCollectionName() {
        return CartMockGeneratorService.COLLECTION_NAME;
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
    public void buildMockRelationShips(Map<String, List<?>> entityMap) {
        List<Map> carts = (List<Map>) entityMap.get(COLLECTION_NAME);
        List<Map> productVariants = (List<Map>) entityMap.get("productVariant");
        List<Map> customers = (List<Map>) entityMap.get("customer");

        carts = carts.stream().peek(cart -> {
            List<Map> cartItems = new ArrayList<>();
            int randomCartSize = faker.random().nextInt(1, 10);

            for (int i = 0; i < randomCartSize; i++) {
                Map cartItem = new HashMap<>();
                Map variant = faker.options().nextElement(productVariants);
                String sku = variant.get("sku").toString();
                int quantity = faker.random().nextInt(1, 10);
                BigDecimal skuPrice = (BigDecimal) variant.get("listPrice");

                cartItem.put("sku", sku);
                cartItem.put("skuPrice", skuPrice);
                cartItem.put("quantity", quantity);
                cartItem.put("total", Math.multiplyExact(skuPrice.longValue(), quantity));

                cartItems.add(cartItem);
            }

            cart.put("cartItems", cartItems);
            cart.put("customerId", faker.options().nextElement(customers).get("id"));

        }).toList();

        entityMap.put(getCollectionName(), carts);
    }


}
