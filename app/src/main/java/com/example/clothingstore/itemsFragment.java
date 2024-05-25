package com.example.clothingstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class itemsFragment extends Fragment implements ProductAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products, filteredProducts;

    private DatabaseReference databaseReference;
    private ValueEventListener productsListener;
    private static final String TAG = "itemsFragment";

    public itemsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        String category = getArguments() != null ? getArguments().getString("category", "all") : "all";
        fetchProducts(category);

        return rootView;
    }

    @Override
    public void onProductClick(Product product) {
        Log.d(TAG, "Sending product ID: " + product.getId());
        Intent intent = new Intent(getActivity(), ItemDetails.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    private void fetchProducts(String category) {
        productsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products = new ArrayList<>();
                filteredProducts = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        products.add(product);
                        if (category.equals("all") || product.getCategory().equalsIgnoreCase(category)) {
                            filteredProducts.add(product);
                        }
                    }
                }
                adapter = new ProductAdapter(filteredProducts, getActivity(), itemsFragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(productsListener);
    }

    public void filterProducts(String query) {
        if (products == null) return;
        filteredProducts.clear();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        if (products == null) return;
        filteredProducts.clear();
        for (Product product : products) {
            if (category.equals("all") || product.getCategory().equalsIgnoreCase(category)) {
                filteredProducts.add(product);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseReference != null && productsListener != null) {
            databaseReference.removeEventListener(productsListener);
        }
    }
}
