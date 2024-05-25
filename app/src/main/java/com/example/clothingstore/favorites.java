package com.example.clothingstore;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class favorites extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapterrr adapter;
    private DatabaseReference favoritesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        favoritesReference = FirebaseDatabase.getInstance().getReference("favorites");

        fetchFavoriteProducts();
    }

    private void fetchFavoriteProducts() {
        favoritesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> favoriteProducts = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null && product.isFavorite()) {
                        favoriteProducts.add(product);
                    }
                }

                adapter = new ProductAdapterrr(favoriteProducts, favorites.this);
                recyclerView.setAdapter(adapter);

                if (favoriteProducts.isEmpty()) {
                    Toast.makeText(favorites.this, "No favorite products", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(favorites.this, "Failed to load favorite products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
