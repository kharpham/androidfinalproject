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

import java.util.ArrayList;

public class BestGameAdapter extends RecyclerView.Adapter<BestGameAdapter.viewholder> {

    ArrayList<Product> products;
    Context context;

    public BestGameAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public BestGameAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_best_deal, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestGameAdapter.viewholder holder, int position) {
        Product p = products.get(position);
        holder.textTitle.setText(p.getProductName());
        holder.textPrice.setText(String.valueOf(p.getProductPrice())+"VND");
        holder.textStar.setText(String.valueOf(p.getStar()));
        Glide.with(context)
                    .load(p.getImagePath())
                    .transform(new CenterCrop(), new RoundedCorners(30))
                    .into(holder.imageProduct);
        holder.textPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product", p);
                context.startActivity(intent);
            }
        });
        holder.itemView.findViewById(R.id.textPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagmentCart managementCart = new ManagmentCart(context);
                managementCart.insertProduct(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        ImageView imageProduct;
        TextView textTitle, textStar, textPlus, textPrice;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textStar = itemView.findViewById(R.id.textStar);
            textPrice = itemView.findViewById(R.id.textPrice);
            textPlus = itemView.findViewById(R.id.textPlus);
            imageProduct = itemView.findViewById(R.id.imageProduct);
        }
    }
}
