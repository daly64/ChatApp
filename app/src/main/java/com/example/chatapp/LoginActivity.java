package com.example.chatapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText email, password;
    Button btn_login;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ToolBox.setToolbar( LoginActivity.this, StartActivity.class, R.id.toolbar, "Login");

        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(v -> {
            ToolBox.hideKeyboard(this);

            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();
            if (txt_email.isEmpty() || txt_password.isEmpty()) {
                Snackbar.make(v, "all fields are required", Snackbar.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6) {
                Snackbar.make(v, "password must be at last 6 characters", Snackbar.LENGTH_SHORT).show();
            } else {
//                login(txt_email, txt_password, v);
                ToolBox.firebaseAuthLogin(this, MainActivity.class, txt_email, txt_password, v, auth);
            }

        });


    }

/*    private void login(String txt_email, String txt_password, View v) {

        auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ToolBox.openActivity(this, MainActivity.class);

                    } else {
                        Snackbar.make(v, "login failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }*/
}