package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.phamnguyenkha.group12finalproject.databinding.PaymentMethodBinding;

public class PaymentMethod extends AppCompatActivity {
    PaymentMethodBinding binding;
    private String selectedPaymentMethod = "cash";
    private boolean paymentMethodSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        forward();
        choose();

    }

    private void choose() {
        binding.radioButtonCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.radioButtonCash.setChecked(false);
                    selectedPaymentMethod = "card";
                    paymentMethodSelected = true;
                }
            }
        });

        binding.radioButtonCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.radioButtonCard.setChecked(false);
                    selectedPaymentMethod = "cash";
                    paymentMethodSelected = true;
                }
            }
        });
    }
    private void sendPaymentMethodIntent() {
        Intent intent = new Intent(this, OrderPlacementActivity.class);
        intent.setAction("PAYMENT_METHOD");
        intent.putExtra("method", selectedPaymentMethod); // Sử dụng biến selectedPaymentMethod thay vì tham số method
        startActivity(intent);
    }

    private void forward() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethod.this,OrderPlacementActivity.class));
            }
        });
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethodSelected) {
                    sendPaymentMethodIntent();
                } else {
                    Toast.makeText(PaymentMethod.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
