package com.phamnguyenkha.group12finalproject.ui.products;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.phamnguyenkha.adapters.Product2Adapter;
import com.phamnguyenkha.group12finalproject.AccountInformationActivity;
import com.phamnguyenkha.group12finalproject.FirebaseManager;
import com.phamnguyenkha.group12finalproject.R;
import com.phamnguyenkha.group12finalproject.databinding.FragmentProductsBinding;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        firebaseManager.getProducts(getContext(),new FirebaseManager.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Product> productList, List<Category>categoryList) {
                // Khi dữ liệu được tải xong, cập nhật danh sách sản phẩm cho adapter
                productAdapter.updateProducts(productList);
                productAdapter.updateCategories(categoryList);
            }
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product selectedProduct = (Product) parent.getItemAtPosition(position);

                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.dialog_product);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                }
                ImageView productImage = dialog.findViewById(R.id.imageProduct);
                TextView productName = dialog.findViewById(R.id.textViewName);
                TextView productPrice = dialog.findViewById(R.id.editTextPrice);
                TextView productId = dialog.findViewById(R.id.productId);
                TextView description = dialog.findViewById(R.id.editTextDescription);

                productImage.setImageResource(selectedProduct.getImagePath());
                productName.setText(selectedProduct.getProductName());
                double price = selectedProduct.getProductPrice();
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price);
                productPrice.setText(formattedPrice);
                String formattedId = "#" + String.valueOf(selectedProduct.getId());
                productId.setText(formattedId);
                description.setText(selectedProduct.getDescription());

                dialog.show();
            }
        });

        View filterLayout = root.findViewById(R.id.filter);
        filterLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN && event.getHistorySize()==0)
                {
                    showPopupMenu(filterLayout);
                    return true;
                }
                return false;
            }
        });
        return root;

    }

    private void showPopupMenu(View filterLayout) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), filterLayout);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.option1) {
                    Toast.makeText(getContext(), "Option 1 selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.option2) {
                    Toast.makeText(getContext(), "Option 2 selected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}