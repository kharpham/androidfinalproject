package com.phamnguyenkha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_placement_item, parent, false);
            holder = new ViewHolder();
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productName = convertView.findViewById(R.id.productName);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.quantity = convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(position);
        // Load image, set name, price, and quantity to corresponding views
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(product.getProductPrice() + " VND");
        holder.quantity.setText("x" + product.getNumberInCart());
        // Load product image here using Glide or any other image loading library
        // Glide.with(context).load(product.getImageUrl()).into(holder.productImage);

        return convertView;
    }

    static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantity;
    }
}

