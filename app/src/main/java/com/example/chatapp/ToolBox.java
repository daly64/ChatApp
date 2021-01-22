package com.example.chatapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ToolBox {

    public static void openActivity(Activity from, Class<?> to, Integer extra) {
        Intent intent = new Intent(from, to);
        intent.putExtra("EXTRA", extra);
        from.startActivity(intent);
        from.finish();
    }

    public static void openActivity(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
        from.finish();
    }

    public static Integer getExtraInt(Activity activity) {
        Intent intent = activity.getIntent();
        return intent.getIntExtra("EXTRA", 0);

    }


    public static void setToolbar(AppCompatActivity activity, Class<?> homeActivity, int Toolbar_id, String title) {
        Toolbar toolbar;
        toolbar = activity.findViewById(Toolbar_id);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v ->
                openActivity(activity, homeActivity));
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //    get device getSerial Number
    @SuppressLint("HardwareIds")
    public static String getSerialNumber() {
        String serialNumber;

        try {
            @SuppressLint("PrivateApi")
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }

    // firebase auth login
    public static void firebaseAuthLogin(Activity activity, Class<?> to, String txt_email, String txt_password, View v, FirebaseAuth auth) {

        auth.signInWithEmailAndPassword(txt_email, txt_password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        openActivity(activity, to);

                    } else {
                        Snackbar.make(v, "login failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    // firebase auth register
    public static void firebaseAuthRegister(Activity activity, Class<?> to, String username, String email, String password, View v, FirebaseAuth auth) {

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
                                openActivity(activity, to);
                            }

                        });

                    }
                });
    }


}
