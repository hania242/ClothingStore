package com.example.clothingstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SectionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton btnMen = view.findViewById(R.id.btnMen);
        ImageButton btnWomen = view.findViewById(R.id.btnWomen);
        ImageButton btnKids = view.findViewById(R.id.btnKids);

        btnMen.setOnClickListener(v -> switchToItemsFragment("men"));
        btnWomen.setOnClickListener(v -> switchToItemsFragment("women"));
        btnKids.setOnClickListener(v -> switchToItemsFragment("kids"));

        return view;
    }

    private void switchToItemsFragment(String category) {
        if (getActivity() instanceof homepage) {
            ((homepage) getActivity()).switchToItemsFragment(category);
        }
    }
}
