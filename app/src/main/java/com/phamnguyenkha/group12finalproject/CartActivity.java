package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.phamnguyenkha.adapters.CartAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityCartBinding;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;
import com.phamnguyenkha.helpers.ChangeNumberItemsListener;
import com.phamnguyenkha.helpers.ManagmentCart;

public class CartActivity extends AppCompatActivity {
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

        setVariable();
        calculateCart();
        initList();


    }

    private void initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }
        else {
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        });
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;

        tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) /100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        binding.subtotal.setText(itemTotal+"VND");
        binding.totalTax.setText(tax+"VND");
        binding.delivery.setText(delivery+"VND");
        binding.total.setText(total+"VND");


    }

    private void setVariable() {
        binding.btnBack.setOnClickListener(v -> finish());

    }
}