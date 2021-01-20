package com.example.chatapp;

import android.content.Context;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.models.Message;
import com.example.chatapp.models.MessageRecycler;

import java.util.List;

public class ToolBox {
    public static void sendMessage(EditText editText, List<Message> messagesList,
                                   Context context, RecyclerView recyclerView){

        RecyclerView.Adapter adapter;
        Message message = new Message(editText.getText().toString());
        messagesList.add(message);
        adapter = new MessageRecycler(context, messagesList);
        recyclerView.setAdapter(adapter);
        editText.setText("");
    }
}
