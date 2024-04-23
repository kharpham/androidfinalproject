package com.phamnguyenkha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;
import com.phamnguyenkha.group12finalproject.R;

import java.util.List;

public class Product2Adapter extends BaseAdapter {
    private Context context;
    private List<Product> productList;
    private LayoutInflater inflater;
    int product_item;
    private List<Category> categoryList;

    public Product2Adapter(Context context, int product_item, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.product_item = product_item;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_item, parent, false);
            holder = new ViewHolder();

            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productName = convertView.findViewById(R.id.textViewNameProduct);
            holder.productStar = convertView.findViewById(R.id.productStar);
            holder.productPrice = convertView.findViewById(R.id.textViewPrice);
            holder.productId = convertView.findViewById(R.id.textViewId);
            holder.productCategory = convertView.findViewById(R.id.textViewCategoryy);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        // Thiết lập hình ảnh cho ImageView
        holder.productImage.setImageResource(product.getImagePath());
        holder.productName.setText(product.getProductName());
        holder.productStar.setText(String.valueOf(product.getStar()));
        holder.productPrice.setText(String.valueOf(product.getProductPrice()));
        holder.productId.setText(String.valueOf("#" + product.getId()));
//        holder.productCategory.setText(String.valueOf(product.getCategoryId()));
//        holder.productCategory.setText(String.valueOf(product.getCategoryName()));
        String categoryName = "";
        for (Category category : categoryList) {
            if (category.getId() == product.getCategoryId()) {
                categoryName = category.getCategoryName();
                break;
            }
        }
        holder.productCategory.setText(categoryName);        return convertView;
    }

    public void updateProducts(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
    public void updateCategories(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productStar;
        TextView productPrice;
        TextView productId;
        TextView productCategory;
    }
}
