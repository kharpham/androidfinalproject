package com.phamnguyenkha.group12finalproject.ui.products;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private List<Category> categoryList;
    private List<Category> CategoryList;
    private List<Product> ProductList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductViewModel productViewModel =
                new ViewModelProvider(this).get(ProductViewModel.class);

        binding = FragmentProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FloatingActionButton addButton = root.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogToAddProduct();


            }
        });
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
                CategoryList = categoryList;
                ProductList = productList;
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
                Spinner categorySpinner = dialog.findViewById(R.id.editCategory);

                productImage.setImageResource(selectedProduct.getImagePath());
                productName.setText(selectedProduct.getProductName());
                double price = selectedProduct.getProductPrice();
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(price);
                productPrice.setText(formattedPrice);
                String formattedId = "#" + String.valueOf(selectedProduct.getId());
                productId.setText(formattedId);
                description.setText(selectedProduct.getDescription());
                int selectedCategoryId = selectedProduct.getCategoryId();
                List<String> categoryNameList = new ArrayList<>();
                for (Category category : CategoryList) {
                    categoryNameList.add(category.getCategoryName());
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNameList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                categorySpinner.setAdapter(categoryAdapter);
                int selectedPosition = -1;
                for (int i = 0; i < CategoryList.size(); i++) {
                    if (CategoryList.get(i).getId() == selectedCategoryId) {
                        selectedPosition = i;
                        break;
                    }
                }
                categorySpinner.setSelection(selectedPosition);

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

    private void showDialogToAddProduct() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_product);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }

        EditText productNameEditText = dialog.findViewById(R.id.textViewName);
        EditText productPriceEditText = dialog.findViewById(R.id.editTextPrice);
        EditText descriptionEditText = dialog.findViewById(R.id.editTextDescription);
        Spinner categorySpinner = dialog.findViewById(R.id.editCategory);

        dialog.show();
    }



    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    private void filterProductsByCategory(Category selectedCategory) {
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : ProductList) {
            if (product.getCategoryId() == selectedCategory.getId()) {
                filteredProducts.add(product);
            }
        }
        // Cập nhật danh sách sản phẩm trên ListView hoặc RecyclerView
        productAdapter.updateProducts(filteredProducts);
    }
    private Category getCategoryById(int categoryId) {
        for (Category category : CategoryList) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }
    private void showPopupMenu(View filterLayout) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), filterLayout);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.option1) {
                    // Option 1 selected, add submenu items
                    SubMenu subMenu = item.getSubMenu();
                    if (subMenu != null) {
                        subMenu.clear();
                        // Thêm các mục từ CategoryList vào submenu
                        for (Category category : CategoryList) {
                            subMenu.add(0, category.getId(), Menu.NONE, category.getCategoryName());
                        }
                        // Kích hoạt tính năng kiểm tra và kích hoạt nhóm để chúng có thể được chọn
                        subMenu.setGroupCheckable(0, true, true);
                        subMenu.setGroupEnabled(0, true);
                        // Thêm sự kiện lắng nghe cho mỗi mục trong submenu để lọc danh sách sản phẩm
                        for (int i = 0; i < subMenu.size(); i++) {
                            MenuItem subMenuItem = subMenu.getItem(i);
                            subMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    // Lọc danh sách sản phẩm theo category được chọn
                                    int categoryId = menuItem.getItemId();
                                    Category selectedCategory = getCategoryById(categoryId);
                                    if (selectedCategory != null) {
                                        filterProductsByCategory(selectedCategory);
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }
                    }
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
