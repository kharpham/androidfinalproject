    package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;

    public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
=======
        setContentView(R.layout.payment_method);
>>>>>>> 0583f040511d3d2b532c0003285e16ef7e5913ef
    }
}