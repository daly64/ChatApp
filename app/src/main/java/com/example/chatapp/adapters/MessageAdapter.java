package com.example.chatapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages;
    private String imageURL;
    private FirebaseUser user;

    public static int MSG_TYPE_LEFT = 0;
    public static int MSG_TYPE_RIGHT = 1;

    public MessageAdapter(Context context, List<Message> messages, String imageURL) {
        this.context = context;
        this.messages = messages;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.show_message.setText(message.getMessage());
        if (holder.profile_image != null) {
            if (imageURL.equals("default")) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(imageURL).into(holder.profile_image);
            }
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(user.getUid())) {
            return MSG_TYPE_RIGHT;
        } else return MSG_TYPE_LEFT;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profile_image;
        public TextView show_message;

        public ViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.show_message);

        }

    }


}
