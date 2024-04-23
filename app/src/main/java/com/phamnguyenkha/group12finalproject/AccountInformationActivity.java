package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phamnguyenkha.group12finalproject.databinding.AccountInformationBinding;
import com.phamnguyenkha.models.UserModel;
import com.squareup.picasso.Picasso;

public class AccountInformationActivity extends AppCompatActivity {
    AccountInformationBinding binding;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =AccountInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        forwardInfo();
    }
    protected void onStart() {
        super.onStart();
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
    }

    private void getData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("avatars").child(userId).child("user_avatar.jpg");
            com.google.firebase.firestore.FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel != null) {
                        binding.textView.setText(userModel.getName());

                        if (binding.imageViewAvatar != null) {
                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Tải hình ảnh từ Firebase Storage bằng Picasso
                                Picasso.get().load(uri).into(new com.squareup.picasso.Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        Bitmap circularBitmap = getCircleBitmap(bitmap);
                                        binding.imageViewAvatar.setImageBitmap(circularBitmap);
                                    }
                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                        // Xử lý khi không thể tải hình ảnh
                                    }
                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                            }).addOnFailureListener(exception -> {
                                // Xử lý khi không thể tải hình ảnh từ Firebase Storage
                            });
                        }
                    }
                } else {
                    Toast.makeText(AccountInformationActivity.this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(AccountInformationActivity.this, "Không thể lấy dữ liệu từ Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        // Tạo một bitmap mới với cùng kích thước và format như bitmap ban đầu
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Tạo một paint để vẽ
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Tạo một hình ảnh làm mask hình tròn
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float radius = Math.min(rectF.width() / 2, rectF.height() / 2);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        // Thiết lập chế độ vẽ chỉ vẽ phần overlap giữa ảnh gốc và hình tròn
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Vẽ bitmap lên canvas
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    private void forwardInfo() {
        binding.forwardUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });
        binding.forwardUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,PersonalInformationActivity.class));
            }
        });
        binding.forwardOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,MyOrdersActivity.class));
            }
        });
        binding.forwardAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInformationActivity.this,AccountSettingActivity.class));
            }
        });

    }


}