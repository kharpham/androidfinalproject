package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phamnguyenkha.group12finalproject.databinding.ActivityEditPhoneNumberBinding;
import com.phamnguyenkha.models.UserModel;

public class EditPhoneNumber extends AppCompatActivity {
    ActivityEditPhoneNumberBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            getData();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        setupListeners();
    }

    private void getData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            com.google.firebase.firestore.FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel != null) {
                        binding.edtPhoneNumber.setText(userModel.getNumber());
                    }
                } else {
                    Toast.makeText(this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Không thể lấy dữ liệu từ Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupListeners() {
        binding.imageViewBack.setOnClickListener(v -> {
            startActivity(new Intent(EditPhoneNumber.this, PersonalInformationActivity.class));
        });

        binding.textViewSave.setOnClickListener(v -> {
            save();
        });

    }
    // Phương thức kiểm tra số điện thoại hợp lệ
    private boolean isValidPhoneNumber(String number) {
        // Kiểm tra số điện thoại có đúng 10 ký tự và bắt đầu bằng số 0
        return number.matches("^0[0-9]{9}$");
    }
    private void save() {
            binding.textViewSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy dữ liệu mới từ EditText
                    String number = binding.edtPhoneNumber.getText().toString().trim();

                    // Kiểm tra điều kiện số điện thoại
                    if (isValidPhoneNumber(number)) {
                        // Số điện thoại hợp lệ, tiếp tục xử lý
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("users").document(userId);
                        userRef.update("number", number).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Xử lý khi cập nhật thành công
                                Toast.makeText(EditPhoneNumber.this, "Số điện thoại đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditPhoneNumber.this, PersonalInformationActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi cập nhật thất bại
                                Toast.makeText(EditPhoneNumber.this, "Không thể cập nhật số điện thoại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Hiển thị thông báo khi số điện thoại không hợp lệ
                        Toast.makeText(EditPhoneNumber.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }