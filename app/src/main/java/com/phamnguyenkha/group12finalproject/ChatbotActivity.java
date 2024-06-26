package com.phamnguyenkha.group12finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phamnguyenkha.group12finalproject.databinding.ActivityChatbotBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;
    ActivityChatbotBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView, EditText, and FloatingActionButton
        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idRLMsg);
        sendMsgFAB = findViewById(R.id.idFABSend);
        chatsModalArrayList = new ArrayList<>();

        // Set up RecyclerView
        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(layoutManager);
        chatsRV.setAdapter(chatRVAdapter);

        // Set up OnClickListener for send button
        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ChatbotActivity.this, "Hãy nhập câu hỏi của bạn !", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setSelectedItemId(R.id.nav_chatbot); // Chọn mục "Chatbot" mặc định

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ChatbotActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                // startActivity(new Intent(ChatbotActivity.this, ChatbotActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(ChatbotActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_notion) {
                // startActivity(new Intent(AccountInformationActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_info) {
                startActivity(new Intent(ChatbotActivity.this, AccountInformationActivity.class));
                return true;
            }
            return false;
        });

    }

    private void getResponse(String message) {
        chatsModalArrayList.add(new ChatsModal(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=181605&key=bio1ZgMXbp9Lukyn&uid=[uid]&msg=" + message;
        String BASE_URL = "http://api.brainshop.ai/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String botResponse = response.body().getBodyContent();

                    chatsModalArrayList.add(new ChatsModal(botResponse, BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ChatbotActivity.this, "Failed to get response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                Toast.makeText(ChatbotActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}