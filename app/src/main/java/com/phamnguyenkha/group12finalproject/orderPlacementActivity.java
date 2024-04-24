package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.phamnguyenkha.group12finalproject.databinding.ActivityOrderPlacementBinding;

public class orderPlacementActivity extends AppCompatActivity {
    ActivityOrderPlacementBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}