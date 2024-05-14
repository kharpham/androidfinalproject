package com.phamnguyenkha.group12finalproject.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.phamnguyenkha.group12finalproject.FirebaseManager;
import com.phamnguyenkha.group12finalproject.OrderPlacementActivity;
import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.group12finalproject.databinding.FragmentStatisticsBinding;
import com.phamnguyenkha.models.Order;
import com.phamnguyenkha.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;
    int maleCount = 0;
    int femaleCount = 0;
    int unknownCount = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);

        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadData();
        updateTopProducts();
        return root;
    }

    private void updateTopProducts() {
       binding.updateTopProducts.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               saveProductsToFirebase();
           }
       });
    }

    private void saveProductsToFirebase() {
        String topProductsForMen = binding.listPopularProductM.getText().toString();
        String topProductsForWomen = binding.listPopularProductW.getText().toString();
        String topProductsForUnknown = binding.listPopularProductU.getText().toString();

        // Chuyển đổi dữ liệu thành danh sách các sản phẩm
        List<String> productsForMen = convertTextToList(topProductsForMen);
        List<String> productsForWomen = convertTextToList(topProductsForWomen);
        List<String> productsForUnknown = convertTextToList(topProductsForUnknown);

        // Tạo map để lưu dữ liệu
        Map<String, Object> data = new HashMap<>();
        data.put("topProductsForMen", productsForMen);
        data.put("topProductsForWomen", productsForWomen);
        data.put("topProductsForUnknown", productsForUnknown);

        // ID của tài liệu cần cập nhật
        String documentId = "productStats";

        // Lưu dữ liệu lên Firestore
        FirebaseFirestore.getInstance().collection("productClassificationByGender")
                .document(documentId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Phương thức chuyển đổi dữ liệu từ TextView thành List<String>
    private List<String> convertTextToList(String text) {
        List<String> list = new ArrayList<>();
        if (text != null && !text.isEmpty()) {
            String[] products = text.split("   "); // Dùng ba khoảng trắng làm dấu phân cách
            for (String product : products) {
                list.add(product.replace("#", "").trim());
            }
        }
        return list;
    }

    //    private void setupPieChart() {
//        PieChart pieChart = binding.pieChart;
//
//        // Tính tổng số lượng nam, nữ và chưa rõ giới tính
//        int totalUsers = maleCount + femaleCount + unknownCount;
//
//
//        // Tạo danh sách các Entry
//        List<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry((float) maleCount / totalUsers * 100, "male"));
//        entries.add(new PieEntry((float) femaleCount / totalUsers * 100,"female"));
//        entries.add(new PieEntry((float) unknownCount / totalUsers * 100,"unknown"));
//
//        // Tạo một PieDataSet từ danh sách các Entry
//        PieDataSet dataSet = new PieDataSet(entries, "Gender Distribution");
//
//        // Tạo danh sách các màu cho các phần tử trong biểu đồ
//        ArrayList<Integer> colors = new ArrayList<>();
//        colors.add(Color.BLUE);       // Màu cho nam
//        colors.add(Color.MAGENTA);    // Màu cho nữ
//        colors.add(Color.GRAY);       // Màu cho chưa rõ giới tính
//
//        // Thiết lập các màu cho các phần tử trong biểu đồ
//        dataSet.setColors(colors);
//
//        // Thiết lập màu sắc và kích thước cho vùng lỗ trống ở giữa biểu đồ
//        pieChart.setHoleColor(Color.WHITE);  // Màu của lỗ trống
//        pieChart.setHoleRadius(40f);         // Bán kính của lỗ trống
//        pieChart.setDrawHoleEnabled(true);   // Cho phép hiển thị lỗ trống
//        pieChart.setTransparentCircleRadius(0); // Tắt vùng vòng tròn trong suốt
//
//        // Tạo một PieData từ PieDataSet
//        PieData pieData = new PieData(dataSet);
//
//        // Thiết lập dữ liệu cho biểu đồ tròn
//        pieChart.setData(pieData);
//        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
//        pieChart.invalidate();  // Cập nhật biểu đồ
//    }
    private void setupPieChart() {
        PieChart pieChart = binding.pieChart;

        // Tính tổng số lượng nam, nữ và chưa rõ giới tính
        int totalUsers = maleCount + femaleCount + unknownCount;

        // Tạo danh sách các Entry
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) maleCount / totalUsers * 100, "male"));
        entries.add(new PieEntry((float) femaleCount / totalUsers * 100, "female"));
        entries.add(new PieEntry((float) unknownCount / totalUsers * 100, "unknown"));

        // Tạo một PieDataSet từ danh sách các Entry
        PieDataSet dataSet = new PieDataSet(entries, "Gender Distribution");

        // Thiết lập màu sắc cho các phần tử trong biểu đồ
        dataSet.setColors(new int[]{Color.parseColor("#1CA7E6"), Color.parseColor("#E63A53"), Color.parseColor("#d6d6d6")});
        dataSet.setValueTextSize(16f);
        // Tạo một PieData từ PieDataSet
        PieData pieData = new PieData(dataSet);

        // Thiết lập dữ liệu cho biểu đồ tròn
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false); // Tắt mô tả mặc định
        pieChart.setDrawEntryLabels(false); // Tắt nhãn cho mỗi phần tử

        // Thiết lập vị trí của mô tả
        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(16f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(40f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(0);
        pieChart.animateY(1500, Easing.EaseInOutQuad);


        pieChart.invalidate();  // Cập nhật biểu đồ
    }






    private void loadData() {
        FirebaseManager firebaseManager = new FirebaseManager();
        firebaseManager.getUserCount(new FirebaseManager.OnUserCountListener() {
            @Override
            public void onUserCount(int userCount) {

             binding.numbUser.setText(String.valueOf(userCount));

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        firebaseManager.getUserGenders(new FirebaseManager.OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(List<UserModel> userModelList) {

                for (UserModel userModel : userModelList) {
                    if (userModel.getGender() == 0) {
                        maleCount++;
                    } else if (userModel.getGender() == 1) {
                        femaleCount++;
                    }
                    else unknownCount++;
                }
                setupPieChart();
//                binding.textView42.setText(String.valueOf(maleCount +"_"+femaleCount+"-"+unknownCount));
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi nếu có
            }
        });
        firebaseManager.classifyOrdersAndFindPopularProductsByGender(new FirebaseManager.OnGenderClassificationListener() {
            @Override
            public void onGenderClassificationCompleted(Map<Integer, Integer> maleOrderCount, Map<Integer, Integer> femaleOrderCount, Map<Integer, Integer> unknownGenderOrderCount) {
                // Hiển thị thông tin số lượng đơn hàng theo từng giới tính
                binding.orderAmount1.setText(String.valueOf(maleOrderCount.getOrDefault(0, 0))); // Nam
                binding.numbOrderMale.setText(String.valueOf(maleOrderCount.getOrDefault(0, 0))); // Nam
                binding.orderAmount2.setText(String.valueOf(femaleOrderCount.getOrDefault(1, 0))); // Nữ
                binding.numbOrderFemale.setText(String.valueOf(femaleOrderCount.getOrDefault(1, 0))); // Nữ
                binding.orderAmount3.setText(String.valueOf(unknownGenderOrderCount.getOrDefault(2, 0))); // Giới tính không xác định
                binding.numbOderUn.setText(String.valueOf(unknownGenderOrderCount.getOrDefault(2, 0))); // Giới tính không xác định
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi khi xảy ra
            }
        }, new FirebaseManager.OnPopularProductsLoadedListener() {
            @Override
            public void onPopularProductsLoaded(Map<Integer, List<String>> popularProductsByGender) {
                List<String> popularProductsForWomen = popularProductsByGender.getOrDefault(1, new ArrayList<>());
                List<String> popularProductsForMen = popularProductsByGender.getOrDefault(0, new ArrayList<>());
                List<String> popularProductsForUnknown = popularProductsByGender.getOrDefault(2, new ArrayList<>());
                displayPopularProducts(popularProductsForWomen, binding.listPopularProductW);
                displayPopularProducts(popularProductsForMen, binding.listPopularProductM);
                displayPopularProducts(popularProductsForUnknown, binding.listPopularProductU);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


    }

    private void displayPopularProducts(List<String> popularProductsForWomen, TextView listPopularProductW) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String productName : popularProductsForWomen) {
            stringBuilder.append("#").append(productName).append("   ");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        listPopularProductW.setText(stringBuilder.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}