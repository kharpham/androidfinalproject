package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.phamnguyenkha.group12finalproject.databinding.AccountSettingBinding;
import com.phamnguyenkha.helpers.ManagmentCart;

public class AccountSettingActivity extends AppCompatActivity {
    AccountSettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= AccountSettingBinding.inflate(getLayoutInflater() );
        setContentView(binding.getRoot());
        rollBack();
    }

    private void rollBack() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this,AccountInformationActivity.class));
            }
        });

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                clearCart();
                startActivity(new Intent(AccountSettingActivity.this, LoginActivity.class));
            }
        });
    }

    private void clearCart() {
        ManagmentCart managementCart = new ManagmentCart(AccountSettingActivity.this);
        managementCart.clearCart();
    }
}