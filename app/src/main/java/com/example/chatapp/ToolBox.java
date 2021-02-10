package com.example.chatapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.reflect.Method;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_PICTURES;

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

        askStoragePermission(context);
        String currentVersion = getCurrentVersion(context);
        String latestVersion = getLatestVersion();

        if (Double.parseDouble(latestVersion) > Double.parseDouble(currentVersion)) {
            setDialogue(context, "Update", "have to update your application");
        }


    }


    public static void setDialogue(Context context, String title, String msg) {

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            downloadUpdate(context);
        };

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", listener);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();


    }

    private static String getCurrentVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    private static String getLatestVersion() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build());
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        final String[] latestVersion = {remoteConfig.getString("latestVersion")};

        remoteConfig.fetchAndActivate().addOnCompleteListener(task -> latestVersion[0] = remoteConfig.getString("latestVersion"));

        return latestVersion[0];

    }

    static String TAG = "debug";

    private static void downloadUpdate(Context context) {
        Activity activity = (Activity) context;
        View view = activity.getWindow().peekDecorView();
        Snackbar.make(view, "Updating", Snackbar.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference()
                .child("release")
                .child("1.1")
                .child("code-wallpaper-18.png");

        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Log.i(TAG, "the download Url is : " + uri);
                    //   download file
                    downloadFile(context, uri);
                })
                .addOnFailureListener(e -> Log.i(TAG, "Handle any errors"));

    }

    private static void downloadFile(Context context, Uri uri) {
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setDestinationInExternalPublicDir(DIRECTORY_PICTURES + File.separator, "file")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        dm.enqueue(request);
    }

    @SuppressLint("InlinedApi")
    private static void askStoragePermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

}
