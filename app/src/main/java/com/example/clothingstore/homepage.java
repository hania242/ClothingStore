package com.example.clothingstore;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class homepage extends AppCompatActivity {
    private EditText searchEditText;
    private itemsFragment itemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize and load the sections fragment
        if (savedInstanceState == null) {
            loadFragment(new SectionsFragment(), false);
        }

        // Set up search bar
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Send search query to itemsFragment
                if (itemsFragment != null) {
                    itemsFragment.filterProducts(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Set up buttons for favorites and cart
        ImageButton favoritesButton = findViewById(R.id.favorites);
        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, favorites.class);
            startActivity(intent);
        });

        ImageButton cartButton = findViewById(R.id.shoppingcart);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(homepage.this, cart.class);
            startActivity(intent);
        });
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void switchToItemsFragment(String category) {
        itemsFragment = new itemsFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        itemsFragment.setArguments(args);
        loadFragment(itemsFragment, true);
    }
}
