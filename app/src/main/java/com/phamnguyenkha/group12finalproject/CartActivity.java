package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phamnguyenkha.group12finalproject.databinding.ActivityCartBinding;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}