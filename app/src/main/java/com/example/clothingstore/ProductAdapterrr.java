package com.example.clothingstore;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapterrr extends RecyclerView.Adapter<ProductAdapterrr.ViewHolder> {

    private List<Product> products;
    private Context context;
    private DatabaseReference cartReference;
    private static final String TAG = "ProductAdapterrr";

    public ProductAdapterrr(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        cartReference = FirebaseDatabase.getInstance().getReference("added_to_cart");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productcart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice()));
        holder.colorTextView.setText(product.getSelectedColor());
        holder.sizeTextView.setText(product.getSelectedSize());
        holder.quantityText.setText(String.valueOf(product.getUserQuantity()));

        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImageView);

        holder.increaseQuantity.setOnClickListener(v -> {
            int currentQuantity = product.getUserQuantity();
            product.setUserQuantity(currentQuantity + 1);
            holder.quantityText.setText(String.valueOf(product.getUserQuantity()));
            cartReference.child(String.valueOf(product.getId())).setValue(product);
            Log.d(TAG, "Increased Quantity: " + product.getUserQuantity() + " for Product: " + product.getName());
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            int currentQuantity = product.getUserQuantity();
            if (currentQuantity > 1) {
                product.setUserQuantity(currentQuantity - 1);
                holder.quantityText.setText(String.valueOf(product.getUserQuantity()));
                cartReference.child(String.valueOf(product.getId())).setValue(product);
                Log.d(TAG, "Decreased Quantity: " + product.getUserQuantity() + " for Product: " + product.getName());
            }
        });

        holder.removeProductImageView.setOnClickListener(v -> {
            product.setAddedToCart(false);
            product.setUserQuantity(1);
            cartReference.child(String.valueOf(product.getId())).removeValue();
            products.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, products.size());
            Log.d(TAG, "Removed Product: " + product.getName());
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<Product> getProducts() {
        return products;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView colorTextView;
        public TextView sizeTextView;
        public TextView quantityText;
        public ImageView productImageView;
        public ImageView removeProductImageView;
        public TextView increaseQuantity;
        public TextView decreaseQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.price);
            colorTextView = itemView.findViewById(R.id.color);
            sizeTextView = itemView.findViewById(R.id.size);
            quantityText = itemView.findViewById(R.id.quantity);
            productImageView = itemView.findViewById(R.id.product);
            removeProductImageView = itemView.findViewById(R.id.removeProduct);
            increaseQuantity = itemView.findViewById(R.id.increase);
            decreaseQuantity = itemView.findViewById(R.id.decrease);
        }
    }
}
