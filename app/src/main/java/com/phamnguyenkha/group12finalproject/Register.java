package com.phamnguyenkha.group12finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.group12finalproject.databinding.ActivityRegisterBinding;
import com.phamnguyenkha.models.UserModel;

import java.util.Date;
import java.util.regex.Pattern;

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
                if (!binding.checkBoxTerms.isChecked()) {
                    Toast.makeText(Register.this, "Bạn phải đồng ý với các điều khoản trước khi đăng ký", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = binding.fullName.getText().toString().trim();
                String number = binding.phoneNumber.getText().toString().trim();
                String email = binding.emailAddress.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String address = "Chứ cung cấp địa chỉ";
                String avatarUrl = "";
                Date dob = null;
                int gender = 0;
                String confirmation = binding.signup.getText().toString().trim();
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
                if (!binding.checkBoxTerms.isChecked()) {
                    Toast.makeText(Register.this, "Bạn phải đồng ý với các chính sách bảo mật và bản quyền", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Đang đăng ký...");

                // Xóa bỏ thông báo lỗi trước khi kiểm tra điều kiện
                binding.fullName.setError(null);
                binding.phoneNumber.setError(null);
                binding.emailAddress.setError(null);
                binding.password.setError(null);

                // Kiểm tra xem các trường đã được điền đầy đủ chưa
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra tính hợp lệ của email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailAddress.setError("Email không hợp lệ");
                    return;
                }

                // Kiểm tra tính hợp lệ của số điện thoại
                if (!android.util.Patterns.PHONE.matcher(number).matches()) {
                    binding.phoneNumber.setError("Số điện thoại không hợp lệ");
                    return;
                }

                // Kiểm tra mật khẩu
                if (password.length() < 8) {
                    binding.password.setError("Mật khẩu bắt buộc phải ít nhất 8 kí tự");
                    return;
                }

                Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                boolean passwordsMatch = pattern.matcher(password).find();
                if (!passwordsMatch) {
                    binding.password.setError("Mật khẩu phải chứa ký tự viết hoa, chữ thường, số và kí tự đặc biệt");
                    return;
                }

                progressDialog.setMessage("Đang kiểm tra...");
                progressDialog.show();


                // Truy vấn Firebase để kiểm tra thông tin
                firebaseFirestore.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // Nếu có tài khoản với email đã nhập
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    progressDialog.cancel();
                                    Toast.makeText(Register.this, "Email đã được sử dụng, vui lòng chọn email khác", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Nếu không có tài khoản với email đã nhập, tiến hành đăng ký
                                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, LoginActivity.class);
                                                    startActivity(intent);

                                                    firebaseFirestore.collection("users").document(FirebaseAuth.getInstance().getUid()).set(new UserModel(name, number, email, address, avatarUrl, dob, gender));
                                                    progressDialog.cancel();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(Register.this, "Tạo tài khoản thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.e("Error", e.getMessage());
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(Register.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
