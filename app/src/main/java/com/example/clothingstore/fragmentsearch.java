package com.example.clothingstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragmentsearch extends Fragment {

    public fragmentsearch() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton btnMen = rootView.findViewById(R.id.btnMen);
        ImageButton btnWomen = rootView.findViewById(R.id.btnWomen);
        ImageButton btnKids = rootView.findViewById(R.id.btnKids);

        btnMen.setOnClickListener(v -> ((homepage) getActivity()).switchToItemsFragment("men"));
        btnWomen.setOnClickListener(v -> ((homepage) getActivity()).switchToItemsFragment("women"));
        btnKids.setOnClickListener(v -> ((homepage) getActivity()).switchToItemsFragment("kids"));

        return rootView;
    }
}
