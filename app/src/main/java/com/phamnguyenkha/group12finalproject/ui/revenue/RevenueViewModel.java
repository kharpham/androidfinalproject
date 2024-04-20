package com.phamnguyenkha.group12finalproject.ui.revenue;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RevenueViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RevenueViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}