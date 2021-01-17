package com.example.chatapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.models.Message;
import com.example.chatapp.models.MessageRecycler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        Button send = findViewById(R.id.sendBtn);


        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Message> messagesList = new ArrayList<>();

        send.setOnClickListener(v -> {

            Message message = new Message(editText.getText().toString());
            messagesList.add(message);
            adapter = new MessageRecycler(this, messagesList);
            recyclerView.setAdapter(adapter);
        });


    }


}