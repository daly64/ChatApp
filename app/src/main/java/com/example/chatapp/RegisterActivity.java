package com.example.chatapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username, email, password;
    Button btn_register;

    FirebaseAuth auth;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ToolBox.setToolbar(RegisterActivity.this, StartActivity.class, R.id.toolbar, "Register");


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
                Register(txt_username, txt_email, txt_password, v);

            }

        });

    }

    public void Register(String username, String email, String password, View v) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userId = firebaseUser.getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                ToolBox.openActivity(this, MainActivity.class);
                            }

                        });

                    }
                });
    }
}