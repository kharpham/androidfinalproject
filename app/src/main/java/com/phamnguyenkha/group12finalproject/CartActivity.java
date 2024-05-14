package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.adapters.BestGameAdapter;
import com.phamnguyenkha.adapters.CartAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityCartBinding;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;
import com.phamnguyenkha.helpers.ChangeNumberItemsListener;
import com.phamnguyenkha.helpers.ManagmentCart;
import com.phamnguyenkha.models.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartEmptyListener {
    ActivityCartBinding binding;
    RecyclerView.Adapter adapter;
    ManagmentCart managementCart;
    com.google.firebase.firestore.FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseManager firebaseManager = new FirebaseManager();
    private double tax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagmentCart(this);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        calculateCart();
        initList();
        initRecyclerProposeProduct();
        addEvents();
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setSelectedItemId(R.id.nav_cart); // Chọn mục "Giỏ hàng" mặc định

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                startActivity(new Intent(CartActivity.this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                // startActivity(new Intent(CartActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_notion) {
                // startActivity(new Intent(CartActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_info) {
                startActivity(new Intent(CartActivity.this, AccountInformationActivity.class));
                return true;
            }
            return false;
        });



    }

    private void initRecyclerProposeProduct() {
        firebaseManager.classifyOrdersAndFindPopularProductsByGender(new FirebaseManager.OnGenderClassificationListener() {
            @Override
            public void onGenderClassificationCompleted(Map<Integer, Integer> maleOrderCount, Map<Integer, Integer> femaleOrderCount, Map<Integer, Integer> unknownGenderOrderCount) {
                // You can handle gender classification results here if needed
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }, new FirebaseManager.OnPopularProductsLoadedListener() {
            @Override
            public void onPopularProductsLoaded(Map<Integer, List<String>> popularProductsByGender) {
                // Assuming gender values: 0 = Male, 1 = Female, others = Unknown
                List<String> unknownGenderProducts = popularProductsByGender.get(2);
                Log.i("unknownproducts", unknownGenderProducts.toString());
                if (unknownGenderProducts != null) {
                    loadProductsIntoRecyclerView(unknownGenderProducts, binding.recyclerProposeGame);
                    Log.i("load product done", "ok");
                    binding.progressBarProposeGame.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductsIntoRecyclerView(List<String> unknownGenderProducts, RecyclerView recyclerProposeGame) {
        ArrayList<Product> list = new ArrayList<>();
        List<Integer> productIdsInt = unknownGenderProducts.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Log.i("productIdsInt", productIdsInt.toString());
        db.collection("product").whereIn("Id", productIdsInt)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        categories = new ArrayList<>();
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            int Id = ((Long) dc.getDocument().get("Id")).intValue();
                            int ImagePath = getResources().getIdentifier((String) dc.getDocument().get("ImagePath"), "drawable", getPackageName());
                            int CategoryId = ((Long) dc.getDocument().get("CategoryId")).intValue();
                            String ProductName = (String) dc.getDocument().get("ProductName");
                            int BestGame = ((Long) dc.getDocument().get("BestGame")).intValue();
                            String Description = (String) dc.getDocument().get("Description");
                            Object starObj = dc.getDocument().get("Star");
                            int star;

                            if (starObj instanceof Long) {
                                star = ((Long) starObj).intValue();
                            } else if (starObj instanceof Double) {
                                star = ((Double) starObj).intValue();
                            } else {
                                // Handle other cases, such as null or unexpected types
                                star = 0; // Set a default value or handle the case accordingly
                            }
                            double ProductPrice = ((Long) dc.getDocument().get("ProductPrice")).doubleValue();
                            Product product = new Product(Id, ProductName, ProductPrice, BestGame, Description, ImagePath, CategoryId, star, 0);
                            list.add(product);

                        }
                        if (list.size() > 0) {
                            recyclerProposeGame.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            RecyclerView.Adapter adapter = new BestGameAdapter(list);
                            recyclerProposeGame.setAdapter(adapter);
                        }
                        for (Product p : list) {
                            Log.i("Product", p.toString());
                        }
                    }
                });
    }

    private void addEvents() {
        binding.buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderPlacementActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
            Log.i("cart", "empty");
        }
        else {
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.GONE);
            Log.i("cart", "not empty");
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter = new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        }, this);
        binding.cardView.setAdapter(adapter);
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10000;


        tax = Math.round(managementCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) /100;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100;

        DecimalFormat df = new DecimalFormat("#,###");
        binding.subtotal.setText(df.format(itemTotal) + " VND");
        binding.totalTax.setText(df.format(tax) + " VND");
        binding.delivery.setText(df.format(delivery) + " VND");
        binding.total.setText(df.format(total) + " VND");


    }
    @Override
    public void onCartEmptied(boolean isEmpty) {
        if (isEmpty) {
            binding.empty.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.scrollViewCart.setVisibility(View.VISIBLE);
            binding.empty.setVisibility(View.GONE);
        }
    }

}