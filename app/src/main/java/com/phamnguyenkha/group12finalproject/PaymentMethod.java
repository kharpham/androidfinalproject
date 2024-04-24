package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.PaymentMethodBinding;

public class PaymentMethod extends AppCompatActivity {
    PaymentMethodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        forward();
    }
    private void forward() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethod.this,OrderPlacementActivity.class));
            }
        });
    }
}