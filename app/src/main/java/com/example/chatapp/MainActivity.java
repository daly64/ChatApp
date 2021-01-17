package com.example.chatapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editText);
        ListView lv = findViewById(R.id.list_item);
        Button send = findViewById(R.id.sendBtn);

        List<String> your_array_list = new ArrayList<String>();

        send.setOnClickListener(v -> {

            String message = String.valueOf(editText.getText());
            your_array_list.add(message);

            ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_list_item_1,
                            your_array_list);

            lv.setAdapter(arrayAdapter);
        });


    }


}