package com.phamnguyenkha.group12finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatsModal> chatsModalArrayList;
    private Context context;
   public static final int VIEW_TYPE_USER = 0;
   public static final int VIEW_TYPE_BOT = 1;

    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList, Context context) {
        this.chatsModalArrayList = chatsModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_USER:
                View userView = inflater.inflate(R.layout.user_msg_rv_item, parent, false);
                return new ChatViewHolder(userView);
            case VIEW_TYPE_BOT:
                View botView = inflater.inflate(R.layout.bot_msg_rv_item, parent, false);
                return new BotViewHolder(botView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModal chat = chatsModalArrayList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER:
                ((ChatViewHolder) holder).userTV.setText(chat.getMessage());
                break;
            case VIEW_TYPE_BOT:
                ((BotViewHolder) holder).botMsgTV.setText(chat.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatsModalArrayList.get(position).getType();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView userTV;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userTV = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        private TextView botMsgTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
