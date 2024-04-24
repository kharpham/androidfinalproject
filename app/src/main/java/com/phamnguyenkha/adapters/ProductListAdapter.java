package com.phamnguyenkha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.viewholder> {
    ArrayList<Product> products;
    Context context;

    public ProductListAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        public viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
