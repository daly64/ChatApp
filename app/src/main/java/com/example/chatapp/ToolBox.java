package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

}
