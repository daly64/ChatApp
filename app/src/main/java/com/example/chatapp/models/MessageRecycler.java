package com.example.chatapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;

import java.util.List;


public class MessageRecycler extends RecyclerView.Adapter<MessageRecycler.ViewHolder> {
    //    fields
    private Context context;
    private List<Message> messagesList;

    //    constructor
    public MessageRecycler(Context context, List<Message> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text);
        }
    }

    @NonNull
    @Override
    public MessageRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecycler.ViewHolder holder, int position) {
        Message currentMessage = messagesList.get(position);
        String messageText = currentMessage.getMessageText();
        holder.messageTextView.setText(messageText);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}