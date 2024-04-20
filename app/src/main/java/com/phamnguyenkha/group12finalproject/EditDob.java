package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.Timestamp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.phamnguyenkha.group12finalproject.databinding.ActivityEditDobBinding;
import com.phamnguyenkha.models.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDob extends AppCompatActivity {
    ActivityEditDobBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDobBinding.inflate(getLayoutInflater());
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
            startActivity(new Intent(EditDob.this, PersonalInformationActivity.class));
        });

        binding.textViewSave.setOnClickListener(v -> {
            save();
        });
        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày, tháng, năm hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditDob.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                // Đặt ngày đã chọn vào EditText
                                String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                binding.edtDOB.setText(selectedDate);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });
    }

    private void save() {
        binding.textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày sinh từ EditText
                String dobString = binding.edtDOB.getText().toString().trim();

                // Chuyển đổi ngày sinh từ String sang kiểu Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date dob = null;
                try {
                    dob = dateFormat.parse(dobString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Kiểm tra xem ngày sinh có hợp lệ không
                if (dob != null) {
                    // Chuyển đổi Date thành Timestamp
                    Timestamp dobTimestamp = new Timestamp(dob);

                    // Khởi tạo Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Truy cập đến tài liệu của người dùng và cập nhật ngày sinh dưới dạng Timestamp
                    DocumentReference userRef = db.collection("users").document(userId);
                    userRef.update("dob", dobTimestamp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Cập nhật thành công
                                    Toast.makeText(EditDob.this, "Ngày sinh đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditDob.this, PersonalInformationActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(EditDob.this, "Không thể cập nhật ngày sinh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Hiển thị thông báo khi ngày sinh không hợp lệ
                    Toast.makeText(EditDob.this, "Vui lòng chọn ngày sinh!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel != null) {
                        // Lấy ngày sinh từ userModel và cập nhật lên giao diện
                        Date dob = userModel.getDob();
                        if (dob != null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String dobString = dateFormat.format(dob);
                            binding.edtDOB.setText(dobString);
                        } else {
                            binding.edtDOB.setText("Chưa cung cấp ngày sinh");
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