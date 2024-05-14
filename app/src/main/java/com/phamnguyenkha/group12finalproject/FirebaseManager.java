package com.phamnguyenkha.group12finalproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Order;
import com.phamnguyenkha.models.OrderInfo;
import com.phamnguyenkha.models.Product;
import com.phamnguyenkha.models.UserModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirebaseManager {
    private final FirebaseFirestore firestore;

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Product> productList,List<Category>categoryList);
        void onError(String errorMessage);
    }
    public interface OnRevenueLoadedListener {
        void onRevenueLoaded(List<Order> orderList);
        void onError(String errorMessage);
    }
    public  interface OnUsersLoadedListener{
        void onUsersLoaded(List<UserModel> userModelList);
        void  onError(String errorMessage);
    }
    public interface OnUserCountListener {
        void onUserCount(int userCount);
        void onError(String errorMessage);
    }
    public interface OnOrdersLoadedListener {
        void onOrdersLoaded(List<OrderInfo> orderList);
        void onError(String errorMessage);
    }
    public interface OnGenderClassificationListener {
        void onGenderClassificationCompleted(Map<Integer, Integer> maleOrderCount, Map<Integer, Integer> femaleOrderCount, Map<Integer, Integer> unknownGenderOrderCount);
        void onError(String errorMessage);
    }
    public interface OnPopularProductsLoadedListener {
        void onPopularProductsLoaded(Map<Integer, List<String>> popularProductsByGender);
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
    public void getUserCount(final OnUserCountListener listener) {
        firestore.collection("users").get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        int userCount = userTask.getResult().size();
                        listener.onUserCount(userCount);
                    } else {
                        listener.onError("Lỗi tải dữ liệu từ Firebase Firestore: " + userTask.getException().getMessage());
                    }
                });
    }
    public void getUserGenders(OnUsersLoadedListener listener) {
        List<UserModel> userModels = new ArrayList<>();

        firestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            int gender = document.getLong("gender").intValue();
                            UserModel userModel = new UserModel();
                            userModel.setGender(gender);
                            userModels.add(userModel);
                        }
                        listener.onUsersLoaded(userModels);
                    } else {
                        listener.onError("Error getting user genders: " + task.getException().getMessage());
                    }
                });
    }
    public void getOrders(OnOrdersLoadedListener listener) {
        firestore.collection("orders")
                .get()
                .addOnCompleteListener(orderTask -> {
                    if (orderTask.isSuccessful()) {
                        List<OrderInfo> orderList = new ArrayList<>();
                        for (DocumentSnapshot orderDocument : orderTask.getResult()) {
                            // Lấy userId từ đơn hàng
                            String userId = orderDocument.getString("userId");

                            // Lấy danh sách id của sản phẩm từ đơn hàng
                            List<String> productIds = new ArrayList<>();
                            if (orderDocument.contains("products")) {
                                List<HashMap<String, Object>> products = (List<HashMap<String, Object>>) orderDocument.get("products");
                                for (HashMap<String, Object> product : products) {
                                    String productId = String.valueOf(product.get("id"));
                                    productIds.add(productId);
                                }
                            }
                            Log.d("OrderInfo", "User ID: " + userId);
                            Log.d("OrderInfo", "Product IDs: " + productIds.toString());
                            // Tạo đối tượng OrderInfo với thông tin userId và danh sách id sản phẩm
                            OrderInfo order = new OrderInfo(userId, productIds);
                            orderList.add(order);
                        }
                        listener.onOrdersLoaded(orderList);
                    } else {
                        listener.onError("Lỗi tải dữ liệu từ Firebase Firestore: " + orderTask.getException().getMessage());
                    }
                });
    }
    public void classifyOrdersAndFindPopularProductsByGender(OnGenderClassificationListener genderListener, OnPopularProductsLoadedListener productsListener) {
        // Tạo các bản đồ để lưu trữ thông tin
        Map<Integer, Integer> maleOrderCount = new HashMap<>();
        Map<Integer, Integer> femaleOrderCount = new HashMap<>();
        Map<Integer, Integer> unknownGenderOrderCount = new HashMap<>();
        Map<Integer, Map<String, Integer>> genderProductCounts = new HashMap<>();

        // Lấy danh sách người dùng từ Firestore
        firestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> userDocuments = task.getResult().getDocuments();
                        // Duyệt qua danh sách người dùng để lấy giới tính và tạo bản đồ cho từng giới tính
                        for (DocumentSnapshot userDocument : userDocuments) {
                            int gender = userDocument.getLong("gender").intValue();
                            if (gender == 0) {
                                maleOrderCount.put(gender, 0);
                            } else if (gender == 1) {
                                femaleOrderCount.put(gender, 0);
                            } else {
                                unknownGenderOrderCount.put(gender, 0);
                            }
                        }
                        // Lấy danh sách đơn hàng từ Firestore
                        getOrders(new OnOrdersLoadedListener() {
                            @Override
                            public void onOrdersLoaded(List<OrderInfo> orderList) {
                                // Duyệt qua danh sách đơn hàng
                                for (OrderInfo order : orderList) {
                                    // Lấy userId từ đơn hàng
                                    String userId = order.getUserId();
                                    // Lấy giới tính của người dùng từ danh sách đã tạo trước đó
                                    int gender = 0; // Giả sử giới tính là nam mặc định
                                    for (DocumentSnapshot userDocument : userDocuments) {
                                        if (userDocument.getId().equals(userId)) {
                                            gender = userDocument.getLong("gender").intValue();
                                            break;
                                        }
                                    }
                                    // Tăng số lượng đơn hàng của từng giới tính
                                    if (gender == 0) {
                                        maleOrderCount.put(gender, maleOrderCount.getOrDefault(gender, 0) + 1);
                                    } else if (gender == 1) {
                                        femaleOrderCount.put(gender, femaleOrderCount.getOrDefault(gender, 0) + 1);
                                    } else {
                                        unknownGenderOrderCount.put(gender, unknownGenderOrderCount.getOrDefault(gender, 0) + 1);
                                    }


                                    // Tăng số lần xuất hiện của mỗi sản phẩm cho mỗi giới tính
                                    for (String productId : order.getProductIds()) {
                                        if (!genderProductCounts.containsKey(gender)) {
                                            genderProductCounts.put(gender, new HashMap<>());
                                        }
                                        int count = genderProductCounts.get(gender).getOrDefault(productId, 0);
                                        genderProductCounts.get(gender).put(productId, count + 1);
                                    }
                                }

                                // Gửi kết quả số lượng đơn hàng cho từng giới tính
                                genderListener.onGenderClassificationCompleted(maleOrderCount, femaleOrderCount, unknownGenderOrderCount);

                                // Tìm ra những sản phẩm phổ biến nhất cho mỗi giới tính
                                Map<Integer, List<String>> popularProductsByGender = new HashMap<>();
                                for (Map.Entry<Integer, Map<String, Integer>> entry : genderProductCounts.entrySet()) {
                                    List<String> popularProducts = entry.getValue().entrySet().stream()
                                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                            .limit(5)
                                            .map(Map.Entry::getKey)
                                            .collect(Collectors.toList());
                                    popularProductsByGender.put(entry.getKey(), popularProducts);

                                }
                                for (Map.Entry<Integer, List<String>> entry : popularProductsByGender.entrySet()) {
                                    Log.d("PopularProducts", "Gender: " + entry.getKey() + ", Products: " + entry.getValue().toString());
                                }

                                // Gửi kết quả sản phẩm phổ biến cho từng giới tính
                                productsListener.onPopularProductsLoaded(popularProductsByGender);

                            }

                            @Override
                            public void onError(String errorMessage) {
                                // Xảy ra lỗi khi tải danh sách đơn hàng
                                genderListener.onError("Error loading order data: " + errorMessage);
                                productsListener.onError("Error loading order data: " + errorMessage);
                            }
                        });
                    } else {
                        // Xảy ra lỗi khi tải danh sách người dùng
                        genderListener.onError("Error loading user data: " + task.getException().getMessage());
                        productsListener.onError("Error loading user data: " + task.getException().getMessage());
                    }
                });
    }

}
