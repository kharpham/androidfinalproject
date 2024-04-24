package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.phamnguyenkha.adapters.OrderAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityOrderPlacementBinding;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Product;
import com.phamnguyenkha.models.UserModel;

import java.util.ArrayList;

public class OrderPlacementActivity extends AppCompatActivity {
    ActivityOrderPlacementBinding binding;
    OrderAdapter orderAdapter;
    ArrayList<Product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userInfo();
        loadData();
        forward();
    }

    private void forward() {
        binding.confirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPlacementActivity.this,OrderConfirm.class));
            }
        });
    }

    private void loadData() {
        ManagmentCart managementCart = new ManagmentCart(this); // Assuming "this" is the context
        ArrayList<Product> cartProducts = managementCart.getListCart();

        OrderAdapter orderAdapter = new OrderAdapter(this, cartProducts);

        binding.listView.setAdapter(orderAdapter);

        int totalPrice = calculateTotalPrice(cartProducts);
        binding.totalPrice.setText(String.valueOf(totalPrice));
    }

    private int calculateTotalPrice(ArrayList<Product> cartProducts) {
        int totalPrice = 0;
        for (Product product : products) {
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
