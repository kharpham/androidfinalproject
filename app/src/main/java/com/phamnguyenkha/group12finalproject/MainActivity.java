    package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.adapters.CategoryAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Product> products;
    ArrayList<Category> categories = new ArrayList<>();
    CategoryAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadDataFromFireStore();
        addEvents();

        binding.lvCats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("category", categories.get(position).getCategoryName());
                Log.i("category", categories.get(position).getCategoryName());
                intent.putExtra("categoryId", categories.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void loadDataFromFireStore() {
        db.collection("category").orderBy("Id", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        categories = new ArrayList<>();
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                int Id = ((Long) dc.getDocument().get("Id")).intValue();
                                int ImagePath = getResources().getIdentifier((String) dc.getDocument().get("ImagePath"), "drawable", getPackageName());
                                String CategoryName = (String) dc.getDocument().get("CategoryName");
                                categories.add(new Category(Id, CategoryName, ImagePath));
                            }
                        }
                        initAdapter();
                        for (Category category : categories) {
                            Log.i("Category", category.getId() + " - " + category.getCategoryName() + " - " + category.getImagePath());
                        }
                    }
                });
    }

    private void initAdapter() {
        adapter = new CategoryAdapter(MainActivity.this, R.layout.category_layout, categories);
        binding.lvCats.setAdapter(adapter);
    }

    private void addEvents() {
        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }
}