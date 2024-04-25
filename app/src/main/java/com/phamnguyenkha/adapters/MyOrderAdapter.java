package com.phamnguyenkha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.models.Order;
import com.phamnguyenkha.models.Product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyOrderAdapter extends ArrayAdapter<Order> {
    private Context mContext;
    private List<Order> mOrders;

    public MyOrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
        mContext = context;
        mOrders = orders;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.my_orders_item, parent, false);
        }

        Order currentOrder = mOrders.get(position);
        List<Product> products = currentOrder.getProducts();

        if (products != null && !products.isEmpty()) {
            // Lấy sản phẩm đầu tiên trong danh sách
            Product firstProduct = products.get(0);

            // Đặt hình ảnh sản phẩm đầu tiên vào ImageView (trong trường hợp này là orderImageView)

            TextView orderNameTextView = listItem.findViewById(R.id.myOrderName);
            orderNameTextView.setText(firstProduct.getProductName());

            // Tính tổng giá và tổng số lượng của tất cả các sản phẩm trong đơn hàng
            double totalOrderPrice = 0;
            int totalOrderQuantity = 0;
            for (Product product : products) {
                totalOrderPrice += (product.getProductPrice() * product.getNumberInCart());
                totalOrderQuantity += product.getNumberInCart();
            }
            DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
            String formattedPrice = decimalFormat.format(totalOrderPrice);

            TextView orderPriceTextView = listItem.findViewById(R.id.myOrderPrice);
            orderPriceTextView.setText(String.format(Locale.getDefault(), "%s VNĐ", formattedPrice));

            TextView orderIdTextView = listItem.findViewById(R.id.myOrderId);
            orderIdTextView.setText(getFormattedDate(currentOrder.getOrderDate()));
            TextView orderQuantityTextView = listItem.findViewById(R.id.myOrderQuantity);
            orderQuantityTextView.setText(String.valueOf(totalOrderQuantity));
        }

        return listItem;
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

}
