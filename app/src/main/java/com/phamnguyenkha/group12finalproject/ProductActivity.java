package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.adapters.ProductAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityProductBinding;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;
    String category;
    int categoryId;
    ArrayList<Product> products = new ArrayList<>();
    ProductAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
        loadDataFromFireStore(categoryId);
        binding.lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = products.get(position);
                Intent intent = new Intent(ProductActivity.this, DetailActivity.class);
                intent.putExtra("product", p);
                startActivity(intent);
            }
        });
    }
    private void getData() {
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        Log.i("Category received", category);
        categoryId = intent.getIntExtra("categoryId", 0);
        Log.i("Category ID received", String.valueOf(categoryId));
        binding.category.setText(category);
    }

    private void loadDataFromFireStore(int categoryId) {
        db.collection("product").whereEqualTo("CategoryId", categoryId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                int Id = ((Long) dc.getDocument().get("Id")).intValue();
                                int ImagePath = getResources().getIdentifier((String) dc.getDocument().get("ImagePath"), "drawable", getPackageName());
                                String ProductName = (String) dc.getDocument().get("ProductName");
                                double ProductPrice = ((Long) dc.getDocument().get("ProductPrice")).doubleValue();
//                                int Star = ((Double) dc.getDocument().get("Star")).intValue();
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
                                String Description = (String) dc.getDocument().get("Description");
                                int CategoryId = ((Long) dc.getDocument().get("CategoryId")).intValue();
                                int BestGame = ((Long) dc.getDocument().get("BestGame")).intValue();
                                products.add(new Product(Id, ProductName, ProductPrice, BestGame, Description, ImagePath, CategoryId, star));
                            }
                        }
                        initAdapter();
                        Log.i("Finish running adapter", "OK");
                    }
                });

    }

    private void initAdapter() {
        adapter = new ProductAdapter(ProductActivity.this, R.layout.product_layout, products, category);
        binding.lvProducts.setAdapter(adapter);
    }
}