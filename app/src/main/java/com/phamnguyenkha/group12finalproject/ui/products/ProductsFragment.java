package com.phamnguyenkha.group12finalproject.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.phamnguyenkha.adapters.Product2Adapter;
import com.phamnguyenkha.group12finalproject.FirebaseManager;
import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.group12finalproject.databinding.FragmentProductsBinding;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private FragmentProductsBinding binding;
    private ListView listView;
    private Product2Adapter productAdapter;
    private FirebaseManager firebaseManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductViewModel productViewModel =
                new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        productViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        listView = root.findViewById(R.id.lvProducts);
        productAdapter = new Product2Adapter(getContext(), R.layout.product_item, new ArrayList<Product>());
        listView.setAdapter(productAdapter);

        firebaseManager = new FirebaseManager();
        firebaseManager.getProducts(new FirebaseManager.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Product> productList) {
                // Khi dữ liệu được tải xong, cập nhật danh sách sản phẩm cho adapter
                productAdapter.updateProducts(productList);
            }
            public void onError(String errorMessage) {
                // Hiển thị Toast thông báo khi có lỗi xảy ra
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}