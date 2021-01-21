package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;

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


}
