package com.example.clothingstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapterrr adapter;
    private DatabaseReference cartReference;
    private TextView subtotalValue, taxesValue, shippingValue, totalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subtotalValue = findViewById(R.id.subtotal_value);
        taxesValue = findViewById(R.id.taxes_value);
        shippingValue = findViewById(R.id.shipping_value);
        totalValue = findViewById(R.id.total_value);

        Button checkoutButton = findViewById(R.id.checkoutButton);


        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double subtotal = calculateSubtotal();
                double taxes = calculateTaxes(subtotal);
                double shipping = calculateShipping();
                double total = subtotal + taxes + shipping;

                Intent intent = new Intent(cart.this, checkout.class);
                intent.putExtra("subtotal", subtotal);
                intent.putExtra("taxes", taxes);
                intent.putExtra("shipping", shipping);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });

        cartReference = FirebaseDatabase.getInstance().getReference("added_to_cart");

        fetchProductsInCart();
    }

    private void fetchProductsInCart() {
        cartReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Product> productsInCart = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null && product.isAddedToCart()) {
                        productsInCart.add(product);
                    }
                }

                adapter = new ProductAdapterrr(productsInCart, cart.this);
                recyclerView.setAdapter(adapter);

                if (productsInCart.isEmpty()) {
                    Toast.makeText(cart.this, "Your cart is empty", Toast.LENGTH_LONG).show();
                }

                double subtotal = calculateSubtotal();
                double taxes = calculateTaxes(subtotal);
                double shipping = calculateShipping();
                double total = subtotal + taxes + shipping;

                subtotalValue.setText(String.format("$%.2f", subtotal));
                taxesValue.setText(String.format("$%.2f", taxes));
                shippingValue.setText(String.format("$%.2f", shipping));
                totalValue.setText(String.format("$%.2f", total));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(cart.this, "Failed to load cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateSubtotal() {
        double subtotal = 0.0;
        for (Product product : adapter.getProducts()) {
            subtotal += product.getPrice() * product.getUserQuantity();
        }
        return subtotal;
    }

    private double calculateTaxes(double subtotal) {
        return subtotal * 0.05;
    }

    private double calculateShipping() {
        return 5.00;
    }
}
