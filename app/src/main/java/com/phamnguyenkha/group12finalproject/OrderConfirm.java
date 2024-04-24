package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phamnguyenkha.group12finalproject.databinding.OrderConfirmBinding;

public class OrderConfirm extends AppCompatActivity {
    OrderConfirmBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}