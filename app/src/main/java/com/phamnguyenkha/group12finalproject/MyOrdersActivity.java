package com.phamnguyenkha.group12finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.phamnguyenkha.group12finalproject.databinding.MyOrdersBinding;

public class MyOrdersActivity extends AppCompatActivity {
    MyOrdersBinding binding;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rollBack();
        addEvent();
        replaceFragment(new MyOrdersFragment());
        setDeliveryView();
    }
    private void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }
    private boolean isCurrentFragmentDelivery() {
        return currentFragment instanceof MyOrdersFragment;
    }
    private boolean isCurrentFragmentHistory() {
        return currentFragment instanceof MyOrdersHistoryFragment;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (currentFragment instanceof MyOrdersFragment) {
            transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else if (currentFragment instanceof MyOrdersHistoryFragment) {
            transaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
//        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        transaction.replace(R.id.containerLayout, fragment);
        transaction.commit();
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = null;
            if (view.equals(binding.delivery) && !isCurrentFragmentDelivery()) {
                fragment = new MyOrdersFragment();
                setDeliveryView();
            } else if (view.equals(binding.history) && !isCurrentFragmentHistory()) {
                fragment = new MyOrdersHistoryFragment();
                setHistoryView();
            }

            if (fragment != null) {
                replaceFragment(fragment);
                setCurrentFragment(fragment); // Cập nhật fragment hiện tại sau khi thay đổi
            }
        }
    };

    private void addEvent() {
        binding.delivery.setOnClickListener(clickListener);
        binding.history.setOnClickListener(clickListener);
    }
    private void setDeliveryView() {
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.blue));
        binding.textView22.setTypeface(null, Typeface.BOLD);
        binding.textView23.setTextColor(ContextCompat.getColor(this, R.color.gray));
        binding.textView23.setTypeface(null, Typeface.NORMAL);
    }

    private void setHistoryView() {
        binding.view1.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        binding.view2.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding.textView22.setTextColor(ContextCompat.getColor(this, R.color.gray));
        binding.textView22.setTypeface(null, Typeface.NORMAL);
        binding.textView23.setTextColor(ContextCompat.getColor(this, R.color.blue));
        binding.textView23.setTypeface(null, Typeface.BOLD);
    }
    private void rollBack() {
        binding.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrdersActivity.this,AccountInformationActivity.class));
            }
        });
    }
}