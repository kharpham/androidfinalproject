package com.phamnguyenkha.group12finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.phamnguyenkha.adapters.BestGameAdapter;
import com.phamnguyenkha.adapters.CategoryAdapter;
import com.phamnguyenkha.group12finalproject.databinding.ActivityMainBinding;
import com.phamnguyenkha.models.Category;
import com.phamnguyenkha.models.Product;
import com.phamnguyenkha.models.UserModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Product> products;
    ArrayList<Category> categories = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        loadUser();
        initBestGame();
        addEvents();
    }

    private void addEvents() {
        binding.imageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initBestGame() {
        ArrayList<Product> list = new ArrayList<>();
        db.collection("product").whereEqualTo("BestGame", 1)
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
                            list.add(new Product(Id, ProductName, ProductPrice, BestGame, Description, ImagePath, CategoryId, star, 0));

                        }
                        if (list.size() > 0) {
                            binding.recyclerBestGame.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            RecyclerView.Adapter adapter = new BestGameAdapter(list);
                            binding.recyclerBestGame.setAdapter(adapter);
                            binding.progressBarBestGame.setVisibility(View.GONE);
                        }
                        for (Product p : list) {
                            Log.i("Product", p.toString());
                        }


                    }
                });
    }



    private void loadUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert currentUser != null;
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                if (userModel != null) {
                    binding.tvUserName.setText(userModel.getName());
                }
            }
        });
    }
//    private void loadDataFromFireStore() {
//        db.collection("category").orderBy("Id", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
////                        categories = new ArrayList<>();
//                        if (error != null) {
//                            Log.e("Firestore error", error.getMessage());
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                int Id = ((Long) dc.getDocument().get("Id")).intValue();
//                                int ImagePath = getResources().getIdentifier((String) dc.getDocument().get("ImagePath"), "drawable", getPackageName());
//                                String CategoryName = (String) dc.getDocument().get("CategoryName");
//                                categories.add(new Category(Id, CategoryName, ImagePath));
//                            }
//                        }
//                        initAdapter();
//                        for (Category category : categories) {
//                            Log.i("Category", category.getId() + " - " + category.getCategoryName() + " - " + category.getImagePath());
//                        }
//                    }
//                });
//    }

}