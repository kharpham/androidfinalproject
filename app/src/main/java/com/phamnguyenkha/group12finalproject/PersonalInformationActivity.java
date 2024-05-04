package com.phamnguyenkha.group12finalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.ResourcesWrapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;
import com.phamnguyenkha.group12finalproject.databinding.PersonalInformationBinding;
import com.phamnguyenkha.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalInformationActivity extends AppCompatActivity {
    PersonalInformationBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PersonalInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addevent();
        resultLauncher();
        forwardInfo();
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setSelectedItemId(R.id.nav_info); // Sử dụng phương thức này để chọn mục mặc định
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(PersonalInformationActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                startActivity(new Intent(PersonalInformationActivity.this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(PersonalInformationActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_notion) {
                // startActivity(new Intent(PersonalInformationActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_info) {
                 startActivity(new Intent(PersonalInformationActivity.this, AccountInformationActivity.class));
                return true;
            }
            return false;
        });
    }

    private void forwardInfo() {
        binding.nameInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformationActivity.this, ModifyPersonalInformationNameActivity.class));
            }
        });
        binding.genInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformationActivity.this, EditGenderActivity.class));
            }
        });
        binding.addressInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformationActivity.this, EditAddressActivity.class));
            }
        });
        binding.phoneNumberInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformationActivity.this, EditPhoneNumber.class));
            }
        });
        binding.dOBInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformationActivity.this, EditDob.class));
            }
        });
    }

    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            // Đăng ký lắng nghe sự kiện thay đổi dữ liệu
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
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel != null) {
                        binding.textView.setText(userModel.getName());
                        binding.textViewUserName.setText(userModel.getName());
                        binding.textViewPhone.setText(userModel.getNumber());
                        binding.textViewEmail.setText(userModel.getEmail());
                        String address = userModel.getAddress();
                        if (address == null || address.isEmpty()) {
                            address = "Chưa cung cấp thông tin";
                        }
                        binding.textViewAddress.setText(address);
                        binding.textViewGen.setText(convertGenderToString(userModel.getGender()));

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
//
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date dob = userModel.getDob();
                        if (dob != null) {
                            String dobString = dateFormat.format(dob);
                            binding.textViewDOB.setText(dobString);
                        } else {
                            binding.textViewDOB.setText("Chưa cung cấp ngày sinh");
                        }
//                        Toast.makeText(PersonalInformationActivity.this, "Tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PersonalInformationActivity.this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(PersonalInformationActivity.this, "Không thể lấy dữ liệu từ Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String convertGenderToString(int gender) {
        if (gender == 0) {
            return "Nam";
        } else if(gender ==1) {
            return "Nữ";
        }
        else {
            return "Chưa cung cấp thông tin";
        }
    }

    private void resultLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        if (extras != null && extras.containsKey("data")) {
                            Bitmap photo = (Bitmap) extras.get("data");
                            Bitmap squarePhoto = makeSquareBitmap(photo);
                            binding.imageViewAvatar.setImageBitmap(getCircleBitmap(squarePhoto));
                            uploadImageToFirebase(squarePhoto, userId);
                        } else {
                            Uri selectedPhotoUri = result.getData().getData( );
                            try {
                                Bitmap selectedPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri);
                                // Áp dụng hình ảnh vào ImageView với hình dạng bo tròn
                                Bitmap squarePhoto = makeSquareBitmap(selectedPhotoBitmap);
                                binding.imageViewAvatar.setImageBitmap(getCircleBitmap(squarePhoto));
                                uploadImageToFirebase(squarePhoto, userId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }



    private void uploadImageToFirebase(Bitmap bitmap, String userId) {
        // Tạo reference đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://testproject-56cc9.appspot.com");

        // Tạo reference cho ảnh mới được tải lên
        StorageReference imageRef = storageRef.child("avatars").child(userId).child("user_avatar.jpg");

        // Chuyển đổi Bitmap thành byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload ảnh lên Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Lấy đường dẫn của ảnh đã được tải lên
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child("user1");
                databaseRef.child("avatarUrl").setValue(imageUrl);
            });
        }).addOnFailureListener(exception -> {
            // Xử lý khi upload thất bại
        });
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
    private Bitmap makeSquareBitmap(Bitmap bitmap) {
        // Tính toán kích thước mới cho bitmap hình vuông
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height);
        int x = (width - size) / 2;
        int y = (height - size) / 2;
        // Tạo một bitmap mới hình vuông
        return Bitmap.createBitmap(bitmap, x, y, size, size);
    }
    private void showOptionsDialog() {
        // Tạo một AlertDialog để hiển thị các tùy chọn
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ");
        // Thêm các tùy chọn vào danh sách
        String[] options = {"Camera", "Thư viện ảnh"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Chọn chụp ảnh từ camera
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        // Chọn ảnh từ thư viện ảnh
                        dispatchChooseFromGalleryIntent();
                        break;
                }
            }
        });
        // Hiển thị hộp thoại
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        // Tạo Intent để chụp ảnh từ camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Gọi startActivityForResult để chụp ảnh từ camera và chờ kết quả trả về
        activityResultLauncher.launch(takePictureIntent);

    }

    private void dispatchChooseFromGalleryIntent() {
        // Tạo Intent để chọn ảnh từ thư viện ảnh
//        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent pickPhotoIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        // Gọi startActivityForResult để chọn ảnh từ thư viện và chờ kết quả trả về
        activityResultLauncher.launch(pickPhotoIntent);
    }

    private void addevent() {
        // Sự kiện khi người dùng nhấn vào nút hoặc ImageView để chọn ảnh
        binding.changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại hoặc danh sách tùy chọn
                showOptionsDialog();
            }
        });
    }

}