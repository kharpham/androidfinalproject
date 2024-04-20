package com.phamnguyenkha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.models.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    int itemLayout;
    List<Category> categories;

    public CategoryAdapter(Context context, int itemLayout, List<Category> categories) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
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

//            holder.catImage = convertView.findViewById(R.id.catImage);
//            holder.catName = convertView.findViewById(R.id.catName);

            convertView.setTag(holder);
        }
        else holder = (ViewHolder) convertView.getTag();

        Category category = categories.get(position);
        holder.catImage.setImageResource(category.getImagePath());
        holder.catName.setText(category.getCategoryName());

        return convertView;

    }

    public static class ViewHolder {
        ImageView catImage;
        TextView catName;
    }

}