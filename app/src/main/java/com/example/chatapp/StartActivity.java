package com.example.chatapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login, register;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(v -> ToolBox.openActivity(this, RegisterActivity.class));
        login.setOnClickListener(v -> ToolBox.openActivity(this, LoginActivity.class));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        check if user is null
        if (firebaseUser != null) {
            ToolBox.openActivity(this, MainActivity.class);
        }


    }

}