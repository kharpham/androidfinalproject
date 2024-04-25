package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.OrderConfirmBinding;

public class OrderConfirm extends AppCompatActivity {
    OrderConfirmBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        back();
    }

    private void back() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirm.this,OrderPlacementActivity.class));
            }
        });
    }
}