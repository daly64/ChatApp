package com.example.chatapp;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser currentUser;
    DatabaseReference reference;
    ImageButton btn_send;
    EditText text_send;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        recycler_view = findViewById(R.id.recycler_view);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ToolBox.setToolbar(this, MainActivity.class, R.id.toolbar1, "");
        String userId = ToolBox.getExtra(this);

        //        manage send button clicking
        btn_send.setOnClickListener(v -> manageMessage(v, userId));

        //      manage a keyboard send clicking
        text_send.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                manageMessage(v, userId);
                return true;
            }
            return false;
        });


        reference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("chats").push().setValue(hashMap);
    }

    private void manageMessage(View v, String userId) {
        String msg = text_send.getText().toString();
        if (!msg.equals("")) {
            sendMessage(currentUser.getUid(), userId, msg);
        } else {
            Snackbar.make(v, "you can't send empty message", Snackbar.LENGTH_SHORT).show();
        }
        text_send.setText("");
        ToolBox.hideKeyboard(this);
    }
}