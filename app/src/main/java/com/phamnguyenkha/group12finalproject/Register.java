package com.phamnguyenkha.group12finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.phamnguyenkha.group12finalproject.databinding.ActivityRegisterBinding;
import com.phamnguyenkha.models.UserModel;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.fullName.getText().toString().trim();
                String number = binding.phoneNumber.getText().toString().trim();
                String email = binding.emailAddress.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String confirmation = binding.confirmation.getText().toString().trim();
                if (name.length() == 0 || number.length() == 0 || email.length() == 0 || password.length() == 0 || confirmation.length() == 0) {
                    Toast.makeText(Register.this, "Thông tin đăng ký không đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirmation.equals(password)) {
                    Toast.makeText(Register.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Register.this, "Mật khẩu phải tối thiếu 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.checkBox.isChecked()) {
                    Toast.makeText(Register.this, "Bạn phải đồng ý với các chính sách bảo mật và bản quyền", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Đang đăng ký...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, LoginActivity.class);
                                startActivity(intent);
                                firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).set(new UserModel(name, number, email));
                                progressDialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Register.this, "Tạo tài khoản thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("Error", e.getMessage());
                            }
                        });
            }
        });

        binding.goToSignInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, LoginActivity.class));
            }
        });
    }
}
