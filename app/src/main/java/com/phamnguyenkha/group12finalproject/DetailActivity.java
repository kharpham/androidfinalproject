package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.phamnguyenkha.adapters.ProductListAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityDetailBinding;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Product;

public class DetailActivity extends AppCompatActivity {

    Product p;
    ActivityDetailBinding binding;
    int quantity = 1;
    ManagmentCart managementCart;
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

        managementCart = new ManagmentCart(this);

        binding.productImage.setImageResource(p.getImagePath());
        Glide.with(DetailActivity.this).load(p.getImagePath()).into(binding.productImage);
        binding.productName.setText(p.getProductName());
        binding.productPrice.setText(ProductListAdapter.df.format(p.getProductPrice()) + " VND");
        binding.productDescription.setText(p.getDescription());
        binding.ratingBar.setRating(p.getStar());
        binding.tvTotal.setText(ProductListAdapter.df.format((quantity * p.getProductPrice())) + " VND");
        binding.tvQuantity.setText(String.valueOf(quantity));

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                binding.tvQuantity.setText(String.valueOf(quantity));
                binding.tvTotal.setText(ProductListAdapter.df.format((quantity * p.getProductPrice())) + " VND");
            }
        });
        binding.tvDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    binding.tvQuantity.setText(String.valueOf(quantity));
                    binding.tvTotal.setText(ProductListAdapter.df.format((quantity * p.getProductPrice())) + " VND");

                }

            }
        });

        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setNumberInCart(quantity);
                managementCart.insertProduct(p);
            }
        });
    }
}