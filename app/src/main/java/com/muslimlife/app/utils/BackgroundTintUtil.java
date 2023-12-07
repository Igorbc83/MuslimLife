package com.muslimlife.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class BackgroundTintUtil {

    public static void changeTintColor(Context context, View view, int color){
        Drawable buttonDrawable = view.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, ContextCompat.getColor(context, color));
        view.setBackground(buttonDrawable);
    }
}
