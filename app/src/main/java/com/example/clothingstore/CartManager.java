package com.example.clothingstore;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> cartProducts;

    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProductToCart(Product product) {
        product.setAddedToCart(true);
        cartProducts.add(product);
    }

    public List<Product> getProductsInCart() {
        List<Product> inCartProducts = new ArrayList<>();
        for (Product product : cartProducts) {
            if (product.isAddedToCart()) {
                inCartProducts.add(product);
            }
        }
        return inCartProducts;
    }

    public void clearCart() {
        cartProducts.clear();
    }
}
