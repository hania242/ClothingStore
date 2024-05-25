package com.example.clothingstore;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static FavoritesManager instance;
    private List<Product> favoriteProducts;
    private DatabaseReference favoritesReference;

    private FavoritesManager() {
        favoriteProducts = new ArrayList<>();
        favoritesReference = FirebaseDatabase.getInstance().getReference("favorites");
    }

    public static synchronized FavoritesManager getInstance() {
        if (instance == null) {
            instance = new FavoritesManager();
        }
        return instance;
    }

    public void addProductToFavorites(Product product) {
        product.setFavorite(true);
        favoriteProducts.add(product);
        favoritesReference.child(String.valueOf(product.getId())).setValue(product);
    }

    public void removeProductFromFavorites(Product product) {
        product.setFavorite(false);
        favoriteProducts.remove(product);
        favoritesReference.child(String.valueOf(product.getId())).removeValue();
    }

    public List<Product> getFavoriteProducts() {
        return new ArrayList<>(favoriteProducts);
    }
}
