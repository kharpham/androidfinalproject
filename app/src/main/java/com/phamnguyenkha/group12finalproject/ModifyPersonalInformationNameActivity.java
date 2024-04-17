package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phamnguyenkha.group12finalproject.databinding.ModifyPersonalInformationNameBinding;
import com.squareup.picasso.Picasso;

public class ModifyPersonalInformationNameActivity extends AppCompatActivity {
    ModifyPersonalInformationNameBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ModifyPersonalInformationNameBinding.inflate(getLayoutInflater());
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
        crollback();
        save();
    }

    private void save() {
        binding.textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên mới từ EditText
                String name = binding.edtName.getText().toString().trim();

                // Kiểm tra xem tên có rỗng không
                if (!TextUtils.isEmpty(name)) {
                    // Kiểm tra xem tên có chứa ký tự số hoặc ký tự đặc biệt không
                    if (!name.matches("[a-zA-Z\\s]+")) {
                        // Nếu tên chứa ký tự số hoặc ký tự đặc biệt, hiển thị thông báo
                        Toast.makeText(ModifyPersonalInformationNameActivity.this, "Tên không được chứa ký tự số hoặc ký tự đặc biệt!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra xem tên có vượt quá 30 ký tự không
                    if (name.length() > 30) {
                        // Nếu tên quá dài, hiển thị thông báo
                        Toast.makeText(ModifyPersonalInformationNameActivity.this, "Tên không được vượt quá 30 ký tự!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Khởi tạo Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Truy cập đến tài liệu của người dùng và cập nhật tên mới
                    DocumentReference userRef = db.collection("users").document(userId);
                    userRef.update("name", name)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Cập nhật thành công, hiển thị thông báo hoặc thực hiện hành động tiếp theo nếu cần
                                    Toast.makeText(ModifyPersonalInformationNameActivity.this, "Tên đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    // Quay lại màn hình trước đó
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(ModifyPersonalInformationNameActivity.this, "Không thể cập nhật tên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Hiển thị thông báo khi tên rỗng
                    Toast.makeText(ModifyPersonalInformationNameActivity.this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void crollback() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( ModifyPersonalInformationNameActivity.this,PersonalInformationActivity.class));
            }
        });
    }

    protected void onStart() {
        super.onStart();

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
                        binding.edtName.setText(userModel.getName());

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
