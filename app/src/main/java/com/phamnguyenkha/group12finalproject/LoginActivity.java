package com.phamnguyenkha.group12finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.phamnguyenkha.group12finalproject.databinding.ActivityLoginBinding;

public class  LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailAddress.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Thông tin đăng nhập không đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setTitle("Đăng nhập vào tài khoản...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish(); // Close LoginActivity
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailAddress.getText().toString().trim();
                if (email.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Thông tin email không được thiếu", Toast.LENGTH_SHORT).show();
                }
                progressDialog.setTitle("Đang gửi thông tin xác nhận đến email");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Email đặt lại mật khẩu đã được gửi thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Gửi email đặt lại mật khẩu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.goToSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });
    }
}
