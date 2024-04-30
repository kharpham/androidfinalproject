package com.phamnguyenkha.group12finalproject.ui.revenue;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.phamnguyenkha.group12finalproject.FirebaseManager;
import com.phamnguyenkha.group12finalproject.R;

import com.phamnguyenkha.group12finalproject.databinding.FragmentRevenueBinding;
import com.phamnguyenkha.models.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RevenueFragment extends Fragment {

    private FragmentRevenueBinding binding;
    private FirebaseManager firebaseManager;
    private long startTime;
    private long endTime;
    private int year;
    private LineChart lineChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRevenueBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Khởi tạo FirebaseManager
        firebaseManager = new FirebaseManager();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        int lastYear = year - 1;
        binding.currentYearValue.setText(String.valueOf(year));
        binding.lastYearValue.setText(String.valueOf(lastYear));
        binding.currentYear.setBackgroundColor(getResources().getColor(R.color.pink));
        binding.lastYear.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.dataView.setVisibility(View.GONE);
        setupYearLayouts();
        setupMonthSpinner();

        // Gọi phương thức để lấy data
        fetchRevenue();
        fetchRevenueToday();
        return root;
    }

    private void setupYearLayouts() {
        // Thiết lập sự kiện cho năm nay
        binding.currentYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = Calendar.getInstance().get(Calendar.YEAR);
                binding.currentYearValue.setText(String.valueOf(year));
                binding.currentYear.setBackgroundColor(getResources().getColor(R.color.pink));
                binding.lastYear.setBackgroundColor(getResources().getColor(R.color.grey));

                setupMonthSpinner();
                updateDataForSelectedMonth(year, binding.monthSpinner.getSelectedItemPosition());
            }
        });

        // Thiết lập sự kiện cho năm trước
        binding.lastYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = Calendar.getInstance().get(Calendar.YEAR)-1;
                binding.currentYearValue.setText(String.valueOf(year+1));
                binding.lastYear.setBackgroundColor(getResources().getColor(R.color.pink));
                binding.currentYear.setBackgroundColor(getResources().getColor(R.color.grey));

                setupMonthSpinner();
                updateDataForSelectedMonth(year, binding.monthSpinner.getSelectedItemPosition());
            }
        });
    }

    private void setupMonthSpinner() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        // Danh sách các tháng
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        ArrayList<String> displayedMonths = new ArrayList<>();
        boolean isCurrentYear = (currentYear == this.year);

        if (isCurrentYear) {
            for (int i = 0; i <= currentMonth; i++) {
                displayedMonths.add(months[i]);
            }
        } else {
            for (String month : months) {
                displayedMonths.add(month);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, displayedMonths);
        binding.monthSpinner.setAdapter(adapter);

        // Xử lý sự kiện khi người dùng chọn tháng
        binding.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDataForSelectedMonth(year, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.monthSpinner.setSelection(displayedMonths.size() - 1);
    }

    private void updateDataForSelectedMonth(int year, int month) {
        // Cập nhật startTime và endTime dựa trên tháng và năm được chọn
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startTime = startCalendar.getTimeInMillis();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);
        endCalendar.set(Calendar.MONTH, month);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endCalendar.set(Calendar.MINUTE, 59);
        endCalendar.set(Calendar.SECOND, 59);
        endTime = endCalendar.getTimeInMillis();

        fetchRevenue();
    }

    private void fetchRevenue() {
    // Gọi phương thức trong FirebaseManager để lấy dữ liệu doanh thu từ startTime đến endTime
    firebaseManager.getRevenue(startTime, endTime, new FirebaseManager.OnRevenueLoadedListener() {
        @Override
        public void onRevenueLoaded(List<Order> orderList) {

            for (Order order : orderList) {
            }
            String totalRevenueString = calculateTotalRevenue(orderList);
            List<Pair<Double, Integer>> dailyRevenues = calculateDailyRevenues(orderList);

            displayDailyRevenues(dailyRevenues);
            displayRevenue(totalRevenueString);
        }

        @Override
        public void onError(String errorMessage) {
            // Xử lý lỗi khi tải dữ liệu doanh thu không thành công
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    });
}

    private void displayDailyRevenues(List<Pair<Double, Integer>> dailyRevenues) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        lineChart = binding.chart;
        ArrayList<Entry> revenueEntries = new ArrayList<>();
        ArrayList<Entry> orderEntries = new ArrayList<>();

        int dayCounter = 1;

        for (Pair<Double, Integer> dailyRevenue : dailyRevenues) {
            double revenue = dailyRevenue.first;
            int orderCount = dailyRevenue.second;

            revenueEntries.add(new Entry(dayCounter, (float) revenue));
            orderEntries.add(new Entry(dayCounter, orderCount));
            dayCounter++;
        }

        // Tạo một LineDataSet từ danh sách Entry
        LineDataSet revenueDataSet = new LineDataSet(revenueEntries, "Daily Revenues");
        revenueDataSet.setColor(Color.BLUE);
        revenueDataSet.setValueTextColor(Color.RED);
        revenueDataSet.setLineWidth(2f);
        revenueDataSet.setValueTextSize(12f);

        LineDataSet orderDataSet = new LineDataSet(orderEntries, "Daily Orders");
        orderDataSet.setColor(Color.GREEN);
        orderDataSet.setValueTextColor(Color.BLACK);
        orderDataSet.setLineWidth(2f);
        orderDataSet.setValueTextSize(12f);

        // Tạo một LineData chứa các DataSet
        LineData lineData = new LineData(revenueDataSet, orderDataSet);

        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(7);
        lineChart.moveViewToX(currentDay);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // Cập nhật LineChart
        lineChart.invalidate();
        setupChartValueSelectedListener();
    }

    private void setupChartValueSelectedListener() {
        // Thiết lập sự kiện khi người dùng chọn giá trị trên biểu đồ
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int dayIndex = (int) e.getX()-1;
                float revenue = lineChart.getLineData().getDataSetByIndex(0).getEntryForIndex(dayIndex).getY();
                float orderCount = lineChart.getLineData().getDataSetByIndex(1).getEntryForIndex(dayIndex).getY();
                int selectedMonth = binding.monthSpinner.getSelectedItemPosition();
                int selectedYear = year;
                binding.day.setText(formatDay(selectedYear, selectedMonth, dayIndex + 1));
                binding.revenueDaily.setText(formatRevenue(revenue));
                binding.ordersDaily.setText(String.valueOf((int) orderCount));
                binding.dataView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected() {
            }
        });
    }

    private String formatDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private List<Pair<Double, Integer>> calculateDailyRevenues(List<Order> orderList) {
        List<Pair<Double, Integer>> dailyRevenues = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            double totalRevenueOfDay = 0;
            int orderCountOfDay = 0;
            for (Order order : orderList) {
                if (order.getOrderDate() != null) {
                    calendar.setTime(order.getOrderDate());
                    if (calendar.get(Calendar.DAY_OF_MONTH) == i) {
                        totalRevenueOfDay += order.getTotalPrice();
                        orderCountOfDay++;
                    }
                }
            }
            Pair<Double, Integer> dailyRevenuePair = new Pair<>(totalRevenueOfDay, orderCountOfDay);
            dailyRevenues.add(dailyRevenuePair);
        }
        return dailyRevenues;
    }

    private void fetchRevenueToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endTime = calendar.getTimeInMillis();
        firebaseManager.getRevenue(startTime, endTime, new FirebaseManager.OnRevenueLoadedListener() {
            @Override
            public void onRevenueLoaded(List<Order> orderList) {
                for (Order order : orderList) {
                }
                String revenue = calculateTotalRevenue(orderList);

                // Hiển thị tổng doanh thu đã tính được
                displayRevenueToday(revenue);
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý lỗi khi tải dữ liệu doanh thu không thành công
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void  displayRevenueToday(String revenue){
        binding.valueToday.setText(String.valueOf(revenue));
    }
    private void displayRevenue(String totalRevenue) {
        binding.monthlyRevenueValue.setText(String.valueOf(totalRevenue));
    }
    private String calculateTotalRevenue(List<Order> orderList) {
        double totalRevenue = 0.0;
        for (Order order : orderList) {
            totalRevenue += order.getTotalPrice();
        }
        return formatRevenue(totalRevenue);
    }
    private String formatRevenue(double revenue) {
        // Tạo một đối tượng DecimalFormat với mẫu số nguyên (#,###) để hiển thị hàng nghìn
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(revenue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}