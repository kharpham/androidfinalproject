package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.AccountInformationBinding;

public class AccountInformationActivity extends AppCompatActivity {
    AccountInformationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =AccountInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        forwardInfo();
    }

    private void forwardInfo() {
        binding.forwardUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });
        binding.forwardUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });
        binding.forwardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });
        binding.forwardAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });

    }
}