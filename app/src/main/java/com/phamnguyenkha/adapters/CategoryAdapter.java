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
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {

    ArrayList<Category> categories;
    Context context;

    public CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_category, parent, false);

        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        Category c = categories.get(position);
        holder.catName.setText(c.getCategoryName());
        Glide.with(context)
                .load(c.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.catImage);

        holder.catImage.setBackgroundResource(context.getResources().getIdentifier("cat_" + position + "_background", "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        ImageView catImage;
        TextView catName;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            catImage = itemView.findViewById(R.id.catImage);
            catName = itemView.findViewById(R.id.catName);
        }
    }


}
