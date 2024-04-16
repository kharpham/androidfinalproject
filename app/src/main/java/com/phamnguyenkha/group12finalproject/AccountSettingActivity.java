package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.AccountSettingBinding;

public class AccountSettingActivity extends AppCompatActivity {
    AccountSettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        rollBack();
    }

    private void rollBack() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this,AccountInformationActivity.class));
            }
        });

    }
}