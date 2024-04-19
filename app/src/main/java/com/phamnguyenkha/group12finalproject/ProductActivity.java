package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;

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

        getData();
        loadDataFromFireStore(categoryId);
    }

    private void getData() {
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        categoryId = intent.getIntExtra("categoryId", 0);
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
                                double ProductPrice = ((double) dc.getDocument().get("ProductPrice"));
                                int Star = ((Long) dc.getDocument().get("Star")).intValue();
                                String Description = (String) dc.getDocument().get("Description");
                                int CategoryId = ((Long) dc.getDocument().get("CategoryId")).intValue();
                                int BestGame = ((Long) dc.getDocument().get("BestGame")).intValue();
                                products.add(new Product(Id, ProductName, ProductPrice, BestGame, Description, ImagePath, CategoryId, Star));
                            }
                        }
                        initAdapter();
                    }


                });

    }

    private void initAdapter() {
        adapter = new ProductAdapter(ProductActivity.this, R.layout.product_layout, products, category);
        binding.lvProducts.setAdapter(adapter);
    }
}