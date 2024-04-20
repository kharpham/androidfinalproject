package com.phamnguyenkha.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.models.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    Context context;
    int itemLayout;
    List<Product> products;
    String category;

    public ProductAdapter(Context context, int itemLayout, List<Product> products, String category) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.products = products;
        this.category = category;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemLayout, null);

//            holder.productImage = convertView.findViewById(R.id.productImage);
//            holder.productName = convertView.findViewById(R.id.productName);
//            holder.productStar = convertView.findViewById(R.id.productStar);
//            holder.productPrice = convertView.findViewById(R.id.productPrice);
//            holder.category = convertView.findViewById(R.id.category);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product p = products.get(position);

        holder.productImage.setImageResource(p.getImagePath());
        holder.productPrice.setText(String.valueOf(p.getProductPrice()));
        holder.productStar.setText(String.valueOf(p.getStar()));
        holder.productName.setText(p.getProductName());
        holder.category.setText(category);
        Log.i("category", category);

        return convertView;

    }

    public static class ViewHolder {
        ImageView productImage;
        TextView productName, productStar, productPrice, category;
    }
}
