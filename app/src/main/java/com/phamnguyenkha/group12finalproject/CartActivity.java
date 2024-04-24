package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        calculateCart();
        initList();


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
        });
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;

        tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) /100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        binding.subtotal.setText(String.format("%.0f VND", itemTotal));
        binding.totalTax.setText(String.format("%.0f VND", tax));
        binding.delivery.setText(String.format("%.0f VND", delivery));
        binding.total.setText(String.format("%.0f VND", total));


    }


}