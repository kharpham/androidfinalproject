package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.phamnguyenkha.adapters.OrderAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityOrderPlacementBinding;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Order;
import com.phamnguyenkha.models.Product;
import com.phamnguyenkha.models.UserModel;

import java.util.ArrayList;
import java.util.Date;

public class OrderPlacementActivity extends AppCompatActivity {
    ActivityOrderPlacementBinding binding;
    OrderAdapter orderAdapter;
    ArrayList<Product> products;
    ManagmentCart managementCart;
    ArrayList<Product> cartProducts;
    TextView methodTextView;
    private String paymentMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userInfo();
        loadData();
        forward();
        order();
        paymentMethod();
        ManagmentCart managementCart = new ManagmentCart(this);
        ArrayList<Product> cartProducts = managementCart.getListCart();
    }

    private void paymentMethod() {
        methodTextView = findViewById(R.id.method);
        Intent intent = getIntent();
        if (intent.getAction() != null && intent.getAction().equals("PAYMENT_METHOD")) {
            String method = intent.getStringExtra("method");
            if (method != null) {
                if (method.equals("card")) {
                    methodTextView.setText("Tài khoản ngân hàng liên kết");
                    paymentMethod = "card";
                } else if (method.equals("cash")) {
                    methodTextView.setText("Thanh toán khi nhận hàng");
                    paymentMethod = "cash";
                }
            }
        } else {
            methodTextView.setText("Thanh toán khi nhận hàng");
            paymentMethod = "cash";
        }
    }

    private void order() {
        binding.buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    if (cartProducts != null && !cartProducts.isEmpty()) {
                        // Tạo một đơn hàng mới
                        Order newOrder = new Order();
                        newOrder.setUserId(currentUser.getUid());
                        newOrder.setRecipientName(binding.textViewLocation.getText().toString()); // Đặt tên người nhận từ TextView
                        newOrder.setOrderDate(new Date());
                        double totalPrice = calculateTotalPrice(cartProducts);
                        newOrder.setTotalPrice(totalPrice);
                        newOrder.setProducts(cartProducts);
                        newOrder.setPaymentMethod(paymentMethod);

                        saveOrderToFirestore(newOrder);

                        clearCart();
                        Toast.makeText(OrderPlacementActivity.this, "Đơn hàng của bạn đã được đặt thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderPlacementActivity.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearCart() {
        ManagmentCart managementCart = new ManagmentCart(OrderPlacementActivity.this);
        managementCart.clearCart();
    }

    private void saveOrderToFirestore(Order newOrder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .add(newOrder)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderPlacementActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void forward() {
        binding.confirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPlacementActivity.this,OrderConfirm.class));
            }
        });
        binding.paymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPlacementActivity.this,PaymentMethod.class));
            }
        });
    }

    private void loadData() {

         managementCart = new ManagmentCart(this);
         cartProducts = managementCart.getListCart();
        orderAdapter = new OrderAdapter(this, cartProducts);

        binding.listView.setAdapter(orderAdapter);

        int totalPrice = calculateTotalPrice(cartProducts);
        binding.totalPrice.setText(String.valueOf(totalPrice));
    }

    private int calculateTotalPrice(ArrayList<Product> cartProducts) {
        int totalPrice = 0;
        for (Product product : cartProducts) {
            totalPrice += (product.getNumberInCart() * product.getProductPrice());
        }
        return totalPrice;
    }

    private void userInfo() {
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
                        String userInfo = "[" + userModel.getName() + "]" + "[" + userModel.getNumber() + "]" + "\n" + "[" + userModel.getAddress() + "]";

                        binding.textViewLocation.setText(userInfo);
                    }

                } else {
                    Toast.makeText(OrderPlacementActivity.this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(OrderPlacementActivity.this, "Không thể lấy dữ liệu từ Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    }
