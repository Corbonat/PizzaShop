package com.corbi.pizzashop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private DatabaseReference databaseReference;

    private ChildEventListener childEventListener;

    private List<Product> productList;

    public ProductAdapter() {
        super();
        productList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                productList.add(product);

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                int index = getProductIndex(product);
                if (index != -1) {
                    productList.set(index, product);

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                int index = getProductIndex(product);
                if (index != -1) {
                    productList.remove(index);

                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }

    private int getProductIndex(Product product) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(product.getId())) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.titleTextView.setText(product.getTitle());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(product.getPrice() + "р");

        Glide.with(holder.itemView)
                .load(product.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.product_title);
            descriptionTextView = itemView.findViewById(R.id.product_description);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}

