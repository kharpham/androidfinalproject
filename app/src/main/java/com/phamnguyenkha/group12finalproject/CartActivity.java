package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phamnguyenkha.adapters.CartAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityCartBinding;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;
import com.phamnguyenkha.helpers.ChangeNumberItemsListener;
import com.phamnguyenkha.helpers.ManagmentCart;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartEmptyListener {
    ActivityCartBinding binding;
    RecyclerView.Adapter adapter;
    ManagmentCart managementCart;
    private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagmentCart(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        calculateCart();
        initList();
        addEvents();
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setSelectedItemId(R.id.nav_cart); // Chọn mục "Giỏ hàng" mặc định

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                startActivity(new Intent(CartActivity.this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                // startActivity(new Intent(CartActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_notion) {
                // startActivity(new Intent(CartActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_info) {
                startActivity(new Intent(CartActivity.this, AccountInformationActivity.class));
                return true;
            }
            return false;
        });



    }

    private void addEvents() {
        binding.buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderPlacementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
            Log.i("cart", "empty");
        }
        else {
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.GONE);
            Log.i("cart", "not empty");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        }, this);
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10000;


        tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) /100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        DecimalFormat df = new DecimalFormat("#,###");
        binding.subtotal.setText(df.format(itemTotal) + " VND");
        binding.totalTax.setText(df.format(tax) + " VND");
        binding.delivery.setText(df.format(delivery) + " VND");
        binding.total.setText(df.format(total) + " VND");


    }
    @Override
    public void onCartEmptied(boolean isEmpty) {
        if (isEmpty) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.GONE);
        }
    }

}