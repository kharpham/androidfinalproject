package com.phamnguyenkha.group12finalproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.phamnguyenkha.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirebaseManager {
    private final FirebaseFirestore firestore;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Product> productList);
        void onError(String errorMessage);
    }

    public FirebaseManager() {
        // Khởi tạo Firebase database
        firestore = FirebaseFirestore.getInstance();
    }

    public void getProducts( Context context,final OnDataLoadedListener listener) {
        firestore.collection("product").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            int id = document.getLong("Id").intValue();
                            String productName = document.getString("ProductName");
                            double productPrice = document.getDouble("ProductPrice");
                            int bestGame = document.getLong("BestGame").intValue();
                            String description = document.getString("Description");
                            String imagePathName = document.getString("ImagePath");
                            int imagePath = context.getResources().getIdentifier(imagePathName, "drawable", context.getPackageName());
                            int categoryId = document.getLong("CategoryId").intValue();
                            int star = 0;
                            Object starObj = document.get("Star");
                            if (starObj instanceof Long) {
                                star = ((Long) starObj).intValue();
                            } else if (starObj instanceof Double) {
                                star = ((Double) starObj).intValue();
                            }
                            productList.add(new Product(id, productName, productPrice, bestGame, description, imagePath, categoryId, star));
                        }
                        listener.onDataLoaded(productList);
                    } else {
                        listener.onError("Lỗi khi tải dữ liệu từ Firebase Firestore: " + task.getException().getMessage());
                    }
                });
    }

}
