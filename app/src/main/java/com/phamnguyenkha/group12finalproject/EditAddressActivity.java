package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phamnguyenkha.group12finalproject.databinding.ActivityEditAddressBinding;
import com.phamnguyenkha.models.UserModel;

public class EditAddressActivity extends AppCompatActivity {
    ActivityEditAddressBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAddressBinding.inflate(getLayoutInflater());
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
            startActivity(new Intent(EditAddressActivity.this, PersonalInformationActivity.class));
        });

        binding.textViewSave.setOnClickListener(v -> {
            saveAddress();
        });
    }

    private void saveAddress() {
        binding.textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy địa mới từ EditText
                String address = binding.edtAddress.getText().toString().trim();

                // Kiểm tra xem địa chỉ có rỗng không
                if (!TextUtils.isEmpty(address)) {
                    // Khởi tạo Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Truy cập đến tài liệu của người dùng và cập nhật địa chỉ
                    DocumentReference userRef = db.collection("users").document(userId);
                    userRef.update("address",address)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Cập nhật thành công, hiển thị thông báo hoặc thực hiện hành động tiếp theo nếu cần
                                    Toast.makeText(EditAddressActivity.this, "Địa chỉ đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    // Quay lại màn hình trước đó
                                    startActivity(new Intent(EditAddressActivity.this, PersonalInformationActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(EditAddressActivity.this, "Không thể cập nhật địa chỉ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Hiển thị thông báo khi địa rỗng
                    Toast.makeText(EditAddressActivity.this, "Vui lòng nhập đại chỉ!", Toast.LENGTH_SHORT).show();
                }
            }
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
                        binding.edtAddress.setText(userModel.getAddress());
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