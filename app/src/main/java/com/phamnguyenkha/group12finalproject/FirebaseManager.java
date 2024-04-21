package com.phamnguyenkha.group12finalproject;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.phamnguyenkha.models.Product;

import java.util.ArrayList;
import java.util.List;

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

    public void getProducts(final OnDataLoadedListener listener) {
        firestore.collection("product").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        listener.onDataLoaded(productList);
                    } else {
                        listener.onError("Lỗi khi tải dữ liệu từ Firebase Firestore: " + task.getException().getMessage());
                    }
                });
    }
}
