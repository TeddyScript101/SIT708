package com.example.chatbot;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> messages;
    private final String username;

    public ChatAdapter(List<ChatMessage> messages, String username) {
        this.messages = messages;
        this.username = username != null ? username : "U"; // Default if username is null
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser ? 1 : 0;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == 1 ? R.layout.item_user_message : R.layout.item_bot_message;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ChatViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageText.setText(message.text);

        if (holder.viewType == 1 && holder.userAvatar != null) {
            String firstLetter = username.isEmpty() ? "U" :
                    username.substring(0, 1).toUpperCase();
            holder.userAvatar.setText(firstLetter);
            holder.userAvatar.setTextColor(Color.BLACK);
        }

        // Safe layout params adjustment
        if (holder.messageText.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) holder.messageText.getLayoutParams();
            params.gravity = message.isUser ? Gravity.END : Gravity.START;
            holder.messageText.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        final TextView messageText;
        final TextView userAvatar;
        final int viewType;

        public ChatViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.messageText = itemView.findViewById(R.id.chatText);

            // Only look for avatar in user messages
            this.userAvatar = viewType == 1 ?
                    itemView.findViewById(R.id.userAvatar) : null;
        }
    }
}