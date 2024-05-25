package com.example.clothingstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;
    private OnItemClickListener listener;
    private DatabaseReference favoritesReference;
    public interface OnItemClickListener {
        void onProductClick(Product product);

    }

    public ProductAdapter(List<Product> products, Context context, OnItemClickListener listener) {
        this.products = products;
        this.context = context;
        this.listener = listener;
        favoritesReference = FirebaseDatabase.getInstance().getReference("favorites");  // Add this line
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false); // Ensure layout name is correct
        return new ProductViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.imageView);
        holder.ratingBar.setRating(product.getRating());
        holder.favoriteImageView.setImageResource(product.isFavorite() ? R.drawable.is : R.drawable.isnot); // Ensure drawable names are correct
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, favoriteImageView;
        TextView nameTextView, priceTextView;
        RatingBar ratingBar;

        public ProductViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            favoriteImageView = itemView.findViewById(R.id.favorite_button);

            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onProductClick(products.get(position));
                }
            });

            favoriteImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = products.get(position);
                    if (!product.isFavorite()) {
                        product.setFavorite(true);
                        favoritesReference.child(String.valueOf(product.getId())).setValue(product);  // Add this line
                        Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        product.setFavorite(false);
                        favoritesReference.child(String.valueOf(product.getId())).removeValue();  // Add this line
                        Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                    notifyItemChanged(position);
                }
            });
        }
    }
}
