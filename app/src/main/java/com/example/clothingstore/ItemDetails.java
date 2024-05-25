package com.example.clothingstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ItemDetails extends AppCompatActivity {
    private Product currentProduct;
    private DatabaseReference databaseReference;
    private DatabaseReference cartReference;
    private static final String TAG = "ItemDetails";

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView;
    private Spinner colorSpinner, sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        int productId = getIntent().getIntExtra("product_id", -1);
        Log.d(TAG, "Received product ID: " + productId);

        productImageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);
        colorSpinner = findViewById(R.id.color_spinner);
        sizeSpinner = findViewById(R.id.size_spinner);

        databaseReference = FirebaseDatabase.getInstance().getReference("products").child(String.valueOf(productId));
        cartReference = FirebaseDatabase.getInstance().getReference("added_to_cart");


        fetchProductDetails();

        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        ImageButton closeButton = findViewById(R.id.back_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchProductDetails() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentProduct = snapshot.getValue(Product.class);
                if (currentProduct != null) {

                    productNameTextView.setText(currentProduct.getName());
                    productPriceTextView.setText(String.valueOf(currentProduct.getPrice()));
                    productDescriptionTextView.setText(currentProduct.getDescription());
                    Glide.with(ItemDetails.this)
                            .load(currentProduct.getImageUrl())
                            .into(productImageView);

                    populateSpinner(colorSpinner, currentProduct.getColors());
                    populateSpinner(sizeSpinner, currentProduct.getSizes());
                } else {
                    Toast.makeText(ItemDetails.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemDetails.this, "Failed to load product details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void addToCart() {
        if (currentProduct != null) {
            String selectedColor = colorSpinner.getSelectedItem().toString();
            String selectedSize = sizeSpinner.getSelectedItem().toString();
            currentProduct.setSelectedColor(selectedColor);
            currentProduct.setSelectedSize(selectedSize);
            currentProduct.setAddedToCart(true);

            cartReference.child(String.valueOf(currentProduct.getId())).setValue(currentProduct)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ItemDetails.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ItemDetails.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
