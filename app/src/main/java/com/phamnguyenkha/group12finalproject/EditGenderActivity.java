package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phamnguyenkha.group12finalproject.databinding.ActivityEditGenderBinding;

public class EditGenderActivity extends AppCompatActivity {
    ActivityEditGenderBinding binding;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditGenderBinding.inflate(getLayoutInflater());
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

    private void setupListeners() {
        binding.imageViewBack.setOnClickListener(v -> {
            startActivity(new Intent(EditGenderActivity.this, PersonalInformationActivity.class));
        });

        binding.textViewSave.setOnClickListener(v -> {
            saveGender();
        });
    }

    private void saveGender() {
        int selectedId = binding.radioGroupGender.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        int genderValue = -1;

        if (selectedId == R.id.radioButtonMale) {
            genderValue = 0; // Đặt giá trị cho giới tính nam
        } else if (selectedId == R.id.radioButtonFemale) {
            genderValue = 1; // Đặt giá trị cho giới tính nữ
        }

        // Lưu giới tính mới vào Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update("gender", genderValue).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Giới tính đã được cập nhật", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditGenderActivity.this, PersonalInformationActivity.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
                        // Đặt giá trị giới tính lên RadioButton tương ứng
                        if (userModel.getGender() == 0) {
                            binding.radioButtonMale.setChecked(true);
                        } else if (userModel.getGender() == 1) {
                            binding.radioButtonFemale.setChecked(true);
                        }
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

}
