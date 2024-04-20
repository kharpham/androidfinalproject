package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phamnguyenkha.group12finalproject.databinding.ActivityDetailBinding;
import com.phamnguyenkha.models.Product;

public class DetailActivity extends AppCompatActivity {

    Product p;
    ActivityDetailBinding binding;
    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void getIntentExtra() {
        p = (Product) getIntent().getSerializableExtra("product");
    }

    private void setVariable() {
        binding.productImage.setImageResource(p.getImagePath());
        binding.productName.setText(p.getProductName());
        binding.productPrice.setText(String.format("%.0f VND", p.getProductPrice()));
        binding.productDescription.setText(p.getDescription());
        binding.ratingBar.setRating(p.getStar());
        binding.tvTotal.setText(quantity * p.getProductPrice() + "VND");
    }
}