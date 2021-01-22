package com.example.chatapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ToolBox.setToolbar(this, MainActivity.class, R.id.toolbar, "Register");
        toolbar = findViewById(R.id.toolbar);


        username = findViewById(R.id.username);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(v -> {
            ToolBox.hideKeyboard(this);
            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();
            if (txt_username.isEmpty() || txt_email.isEmpty() || txt_password.isEmpty()) {
                Snackbar.make(v, "all fields are required", Snackbar.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6) {
                Snackbar.make(v, "password must be at last 6 characters", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(v, "registration complete", Snackbar.LENGTH_SHORT).show();
                ToolBox.firebaseAuthRegister(this, MainActivity.class, txt_username, txt_email, txt_password, v, auth);
            }

        });

    }


}