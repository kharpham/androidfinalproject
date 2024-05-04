package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phamnguyenkha.group12finalproject.databinding.OrderConfirmBinding;
import com.phamnguyenkha.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class OrderConfirm extends AppCompatActivity {
    OrderConfirmBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        back();
        loadData();
        binding.editTextName.addTextChangedListener(textWatcher);
        binding.editTextPhone.addTextChangedListener(textWatcher);
        binding.editTextAddress.addTextChangedListener(textWatcher);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        confirm();
    }

    private void loadData() {
        String userInfo = getIntent().getStringExtra("USER_INFO");

        binding.textViewLocation.setText(userInfo);
        String[] userInfoParts = userInfo.split("\\]\\[");

        if (userInfoParts.length >= 1) {
            String name = userInfoParts[0].substring(1).trim(); // Loại bỏ dấu "[" ở đầu
            if (name.isEmpty()) {
                binding.editTextName.setText("");
            } else {
                binding.editTextName.setText(name);
            }
        } else {
            binding.editTextName.setText("");
        }

        if (userInfoParts.length >= 2) {
            String number = userInfoParts[1].trim();
            if (number.isEmpty()) {
                binding.editTextPhone.setText("");
            } else {
                binding.editTextPhone.setText(number);
            }
        } else {
            binding.editTextPhone.setText("");
        }

        if (userInfoParts.length >= 3) {
            String address = userInfoParts[2].substring(0, userInfoParts[2].length() - 1).trim(); // Loại bỏ dấu "]" ở cuối
            if (address.isEmpty()) {
                binding.editTextAddress.setText("");
            } else {
                binding.editTextAddress.setText(address);
            }
        } else {
            binding.editTextAddress.setText("");
        }
    }


    private TextWatcher textWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        public void afterTextChanged(Editable s) {
            updateTextView();
        }
    };
    private void updateTextView() {
        // Lấy giá trị từ các EditText
        String name = binding.editTextName.getText().toString();
        String phone = binding.editTextPhone.getText().toString();
        String address = binding.editTextAddress.getText().toString();

        // Format thông tin và gán vào TextView
        binding.textViewLocation.setText( name + " - " + phone + "\n" + address);
    }

    private void back() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirm.this,OrderPlacementActivity.class));
            }
        });
    }
    private void confirm(){
        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }
    private boolean isValidPhoneNumber(String number) {
        // Kiểm tra số điện thoại có đúng 10 ký tự và bắt đầu bằng số 0
        return number.matches("^0[0-9]{9}$");
    }
    private void save() {
        // Lấy dữ liệu mới từ EditText
        String name = binding.editTextName.getText().toString().trim();
        String number = binding.editTextPhone.getText().toString().trim();
        String address = binding.editTextAddress.getText().toString().trim();

        // Kiểm tra xem các trường dữ liệu có hợp lệ không
        if (!name.isEmpty() && !number.isEmpty() && !address.isEmpty() && isValidPhoneNumber(number)) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(userId);
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("number", number);
            updates.put("address", address);

            userRef.update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OrderConfirm.this, "Thông tin đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OrderConfirm.this,OrderPlacementActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý khi cập nhật thất bại
                            Toast.makeText(OrderConfirm.this, "Không thể cập nhật thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Hiển thị thông báo khi dữ liệu không hợp lệ hoặc trống
            Toast.makeText(OrderConfirm.this, "Vui lòng điền đầy đủ và chính xác thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

}