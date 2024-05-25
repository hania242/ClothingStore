package com.example.clothingstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class checkout extends AppCompatActivity {

    private EditText etName, etCity, etStreet, etBuilding, etFloor;
    private TextView subtotalValue, taxesValue, shippingValue, totalValue;
    private Button placeOrderButton;

    private DatabaseReference ordersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        etName = findViewById(R.id.et_name);
        etCity = findViewById(R.id.et_city);
        etStreet = findViewById(R.id.et_street);
        etBuilding = findViewById(R.id.et_building);
        etFloor = findViewById(R.id.et_floor);

        subtotalValue = findViewById(R.id.subtotal_value);
        taxesValue = findViewById(R.id.taxes_value);
        shippingValue = findViewById(R.id.shipping_value);
        totalValue = findViewById(R.id.total_value);

        placeOrderButton = findViewById(R.id.btn_place_order);

        // Initialize Firebase Database reference
        ordersReference = FirebaseDatabase.getInstance().getReference("orders");

        // Get the values from the intent
        Intent intent = getIntent();
        double subtotal = intent.getDoubleExtra("subtotal", 0.0);
        double taxes = intent.getDoubleExtra("taxes", 0.0);
        double shipping = intent.getDoubleExtra("shipping", 0.0);
        double total = intent.getDoubleExtra("total", 0.0);

        // Set the values to TextViews
        subtotalValue.setText(String.format("$%.2f", subtotal));
        taxesValue.setText(String.format("$%.2f", taxes));
        shippingValue.setText(String.format("$%.2f", shipping));
        totalValue.setText(String.format("$%.2f", total));

        // Set an OnClickListener for the place order button
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast message
                Toast.makeText(checkout.this, "Thank you for shopping with Everyday Luxe", Toast.LENGTH_LONG).show();

                // Save order details to Firebase
                saveOrderDetails(subtotal, taxes, shipping, total);

                // Create an Intent to go back to the HomePage
                Intent intent = new Intent(checkout.this, homepage.class);
                // Clear the activity stack to prevent back navigation to the checkout page
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity
                finish();
            }
        });
    }

    private void saveOrderDetails(double subtotal, double taxes, double shipping, double total) {
        String name = etName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String street = etStreet.getText().toString().trim();
        String building = etBuilding.getText().toString().trim();
        String floor = etFloor.getText().toString().trim();

        // Create a unique order ID
        String orderId = ordersReference.push().getKey();

        // Create a map to hold the order details
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("name", name);
        orderDetails.put("city", city);
        orderDetails.put("street", street);
        orderDetails.put("building", building);
        orderDetails.put("floor", floor);
        orderDetails.put("subtotal", subtotal);
        orderDetails.put("taxes", taxes);
        orderDetails.put("shipping", shipping);
        orderDetails.put("total", total);

        // Save the order details to Firebase
        if (orderId != null) {
            ordersReference.child(orderId).setValue(orderDetails);
        }
    }
}
