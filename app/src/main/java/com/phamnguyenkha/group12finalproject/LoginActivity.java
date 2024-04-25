package com.phamnguyenkha.group12finalproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.phamnguyenkha.group12finalproject.databinding.ActivityLoginBinding;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        letUserInIfAlreadyLoggedIn();

        progressDialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailAddress.getText().toString().trim();
                String password = binding.password.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validateEmail(email)) {
                        binding.emailAddress.setError("Email không hợp lệ");
                        return;
                    }

                    if (!validatePassword(password)) {
                        binding.password.setError("Mật khẩu không hợp lệ");
                        return;
                    }

                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (email.equals("admingameon@gmail.com")) {
                                        // Người dùng là admin, chuyển hướng đến AdminActivity
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        saveLoginInfo(email, password); // Lưu thông tin đăng nhập
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                        finish();
                                    } else {
                                        // Người dùng không phải là admin, chuyển hướng đến MainActivity
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        saveLoginInfo(email, password); // Lưu thông tin đăng nhập
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    if (e instanceof FirebaseAuthInvalidUserException || e instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });

        binding.goToSignUpActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });

        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChatbotActivity.class));
            }
        });
    }

    private void letUserInIfAlreadyLoggedIn() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword(String password) {
        return password.length() >= 8 && Pattern.compile("[a-zA-Z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find() && Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);
        emailBox.requestFocus();
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailBox.getText().toString();
                if (TextUtils.isEmpty(emailAddress) || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    Toast.makeText(LoginActivity.this, "Nhập địa chỉ email đã đăng ký tài khoản", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Hãy kiểm tra email của bạn ", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "Thất bại, không thể gửi ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }

    // Phương thức lưu thông tin đăng nhập vào SharedPreferences
    private void saveLoginInfo(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }
}
