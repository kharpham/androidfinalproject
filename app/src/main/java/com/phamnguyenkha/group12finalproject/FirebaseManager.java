package com.phamnguyenkha.group12finalproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.phamnguyenkha.group12finalproject.ui.products.ProductsFragment;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Order;
import com.phamnguyenkha.models.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirebaseManager {
    private final FirebaseFirestore firestore;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Product> productList,List<Category>categoryList);
//        void onOrdersLoaded(List<Order> orderList);
        void onError(String errorMessage);
    }
    public interface OnRevenueLoadedListener {
        void onRevenueLoaded(List<Order> orderList);
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
                            productList.add(new Product(id, productName, productPrice, bestGame, description, imagePath, categoryId, star,-1));
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
    public void getRevenue(long startTime, long endTime, final OnRevenueLoadedListener listener) {
        Log.d("FirebaseManager", "StartTime: " + startTime);
        Log.d("FirebaseManager", "EndTime: " + endTime);

        // Tạo đối tượng Date từ thời gian Unix
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        // Tạo đối tượng Timestamp từ đối tượng Date
        Timestamp startTimestamp = new Timestamp(startDate);
        Timestamp endTimestamp = new Timestamp(endDate);

        firestore.collection("orders")
                .whereGreaterThanOrEqualTo("orderDate", startTimestamp)
                .whereLessThanOrEqualTo("orderDate", endTimestamp)
                .get()
                .addOnCompleteListener(orderTask -> {
                    if (orderTask.isSuccessful()) {
                        List<Order> orderList = new ArrayList<>();
                        for (DocumentSnapshot orderDocument : orderTask.getResult()) {
                            // Lấy giá trị thời gian đặt hàng từ Firebase
                            Timestamp orderTimestamp = orderDocument.getTimestamp("orderDate");
                            long orderTime = orderTimestamp.getSeconds() * 1000; // Chuyển đổi về milliseconds

                            // Kiểm tra xem thời gian đặt hàng có nằm trong khoảng thời gian được chọn không
                            if (orderTime >= startTime && orderTime <= endTime) {
                                // Trích xuất thông tin liên quan đến lợi nhuận từ mỗi đơn hàng
                                double totalPrice = orderDocument.getDouble("totalPrice");
                                // Tạo đối tượng Order từ thông tin đã trích xuất
                                Order order = new Order();
                                order.setTotalPrice(totalPrice);
                                order.setOrderDate(orderTimestamp.toDate());

                                // Thêm đơn hàng vào danh sách
                                orderList.add(order);
                            }
                        }
                        // Gọi phương thức callback và truyền danh sách doanh thu đã tính được
                        listener.onRevenueLoaded(orderList);
                    } else {
                        listener.onError("Lỗi tải dữ liệu từ Firebase Firestore: " + orderTask.getException().getMessage());
                    }
                });
    }




}
