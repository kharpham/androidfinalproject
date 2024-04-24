package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.adapters.CategoryAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityListProductBinding;
import com.phamnguyenkha.group12finalproject.databinding.ActivityProductBinding;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;
import java.util.Collection;

public class ListProductActivity extends AppCompatActivity {
    ActivityListProductBinding binding;
    FirebaseFirestore db;

    int CategoryId;
    String CategoryName;
    String SearchText;
    boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();


    }

    private void initList() {
        ArrayList<Product> list = new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);

        Query ref;
        if (isSearch == false) {
            ref = db.collection("product").orderBy("ProductName").whereEqualTo("CategoryId", CategoryId);
        }
        else {
            ref = db.collection("product").orderBy("ProductName").startAt(SearchText).endAt(SearchText + '\uf8ff');
        }
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                list.add(new Product(Id, ProductName, ProductPrice, BestGame, Description, ImagePath, CategoryId, star, 1));
                            }
                        }
//                        if (list.size() > 0) {
//                            binding.productListView.setLayoutManager(new GridLayoutManager(ListProductActivity.this, 3));
//                            RecyclerView.Adapter adapter = new CategoryAdapter(list);
//                            binding.productListView.setAdapter(adapter);
//                            binding.progressBar.setVisibility(View.GONE);
//                        }
                        Log.i("Finish running adapter", "OK");
                    }
                });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("categoryId", 0);
        categoryName = getIntent().getStringExtra("categoryName");
        searchText = getIntent().getStringExtra("searchText");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.textTitle.setText(categoryName);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
    }
}