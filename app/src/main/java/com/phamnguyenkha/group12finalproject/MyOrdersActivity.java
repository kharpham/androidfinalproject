package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.MyOrdersBinding;

public class MyOrdersActivity extends AppCompatActivity {
    MyOrdersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rollBack();
    }

    private void rollBack() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrdersActivity.this,AccountInformationActivity.class));
            }
        });
    }
}