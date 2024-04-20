package com.phamnguyenkha.group12finalproject.ui.products;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.phamnguyenkha.models.Product;

import java.util.List;

public class ProductViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private DatabaseReference mDatabase;
    private MutableLiveData<List<Product>> productList;

    public ProductViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Products List");
    }

    public LiveData<String> getText() {
        return mText;
    }
}