package com.example.chatapp;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
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
//        Button send = findViewById(R.id.sendBtn);


        recyclerView = findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Message> messagesList = new ArrayList<>();

        Context context = this;
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // Send message
                ToolBox.sendMessage(editText,messagesList,context,recyclerView);


                return false;
            }

            return false;
        });

       /* send.setOnClickListener(v -> {

            Message message = new Message(editText.getText().toString());
            messagesList.add(message);
            adapter = new MessageRecycler(this, messagesList);
            recyclerView.setAdapter(adapter);

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        });*/


    }


}