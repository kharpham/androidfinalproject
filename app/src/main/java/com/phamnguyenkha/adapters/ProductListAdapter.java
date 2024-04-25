package com.phamnguyenkha.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.phamnguyenkha.group12finalproject.DetailActivity;
import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.viewholder> {
    ArrayList<Product> products;
    Context context;
    public static final DecimalFormat df = new DecimalFormat("#,###");

    public ProductListAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_list_product, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.viewholder holder, int position) {
        Product p = products.get(position);


        holder.productName.setText(p.getProductName());
        holder.productStar.setText(String.valueOf(p.getStar()));
        holder.productPrice.setText(df.format(p.getProductPrice()) + " VND");
        Glide.with(context)
                .load(p.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product", p);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productStar;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStar = itemView.findViewById(R.id.productStar);
        }
    }
}
