package com.phamnguyenkha.adapters;

import android.content.Context;
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
import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.helpers.ChangeNumberItemsListener;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Product> products;
    ManagmentCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Product> products, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.products = products;
        managementCart = new ManagmentCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;

    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        Product p = products.get(position);
        holder.productName.setText(p.getProductName());
        holder.productPrice.setText(p.getProductPrice()+"VND");
        holder.totalProductPrice.setText(p.getNumberInCart() + "* " + p.getNumberInCart() * p.getProductPrice() + "VND");
        holder.tvQuantity.setText(String.valueOf(p.getNumberInCart()));
        Glide.with(holder.itemView.getContext())
                .load(p.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.productImage);
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.plusNumberItem(products, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
        holder.tvDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.minusNumberItem(products, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, tvDecrement, tvAdd, tvQuantity, totalProductPrice;
        ImageView productImage;
        public viewholder(@NonNull View itemView) {

            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            totalProductPrice = itemView.findViewById(R.id.totalProductPrice);
            tvAdd = itemView.findViewById(R.id.tvAdd);
            tvDecrement = itemView.findViewById(R.id.tvDecrement);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
