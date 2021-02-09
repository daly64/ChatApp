package com.example.chatapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.lang.reflect.Method;

public class ToolBox {

    public static void openActivity(Activity from, Class<?> to, String extra) {
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

    public static String getExtra(Activity activity) {
        Intent intent = activity.getIntent();
        return intent.getStringExtra("EXTRA");

    }


    public static void setToolbar(AppCompatActivity activity, Class<?> homeActivity, int Toolbar_id, String title) {
        Toolbar toolbar = activity.findViewById(Toolbar_id);
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
            assert serialNumber != null;
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


    public static void autoUpdate(Context context) {
        PackageManager manager = context.getPackageManager();
        Activity activity = (Activity) context;
        View view = activity.getWindow().peekDecorView();

        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build());
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            remoteConfig.fetchAndActivate().addOnCompleteListener(activity, task -> {

                if (task.isSuccessful()) {

                    String currentVersion = info.versionName;
                    String latestVersion = remoteConfig.getString("latestVersion");

                    if (Double.parseDouble(latestVersion) > Double.parseDouble(currentVersion)) {

                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
                        dlgAlert.setMessage("have to update your application");
                        dlgAlert.setTitle("Update");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }

                } else {
                    Snackbar.make(view, "Fetch failed", Snackbar.LENGTH_SHORT).show();
                }

            });

        } catch (
                PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ;

    }
}
