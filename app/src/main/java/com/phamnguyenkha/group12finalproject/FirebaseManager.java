package com.phamnguyenkha.group12finalproject;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.phamnguyenkha.group12finalproject.ui.products.ProductsFragment;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    private final FirebaseFirestore firestore;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Product> productList,List<Category>categoryList);
        void onError(String errorMessage);
    }

    public FirebaseManager() {
        // Khởi tạo Firebase database
        firestore = FirebaseFirestore.getInstance();
    }

    public void getProducts( Context context,final OnDataLoadedListener listener) {
        firestore.collection("product").get()
                .addOnCompleteListener(productTask -> {
                    if (productTask.isSuccessful()) {
                        List<Product> productList = new ArrayList<>();
                        for (DocumentSnapshot productDocument : productTask.getResult()) {
                            int id = productDocument.getLong("Id").intValue();
                            String productName = productDocument.getString("ProductName");
                            double productPrice = productDocument.getDouble("ProductPrice");
                            int bestGame = productDocument.getLong("BestGame").intValue();
                            String description = productDocument.getString("Description");
                            String imagePathName = productDocument.getString("ImagePath");
                            int imagePath = context.getResources().getIdentifier(imagePathName, "drawable", context.getPackageName());
                            int categoryId = productDocument.getLong("CategoryId").intValue();
                            int star = 0;
                            Object starObj = productDocument.get("Star");
                            if (starObj instanceof Long) {
                                star = ((Long) starObj).intValue();
                            } else if (starObj instanceof Double) {
                                star = ((Double) starObj).intValue();
                            }
                            productList.add(new Product(id, productName, productPrice, bestGame, description, imagePath, categoryId, star));
                        }

                        firestore.collection("category").get()
                                .addOnCompleteListener(categoryTask -> {
                                    if (categoryTask.isSuccessful()) {
                                        List<Category> categoryList = new ArrayList<>();
                                        for (DocumentSnapshot categoryDocument : categoryTask.getResult()) {
                                            int categoryId = categoryDocument.getLong("Id").intValue();
                                            String categoryName = categoryDocument.getString("CategoryName");
                                            // Không cần imagePath trong lớp Category nên có thể để -1
                                            categoryList.add(new Category(categoryId, categoryName, -1));
                                        }
                                        // Gọi phương thức callback để trả về cả danh sách sản phẩm và danh sách category
                                        listener.onDataLoaded(productList, categoryList);
                                    } else {
                                        listener.onError("Lỗi tải dữ liệu từ Firebase Firestore: " + categoryTask.getException().getMessage());
                                    }
                                });

                    } else {
                        listener.onError("Lỗi tải dữ liệu từ Firebase Firestore: " + productTask.getException().getMessage());
                    }
                });
    }

}
