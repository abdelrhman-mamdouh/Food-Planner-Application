package com.example.foodzarella;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {

    public static void showSnackbar(Context context, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        snackbar.show();
    }

    public static void showTopSnackbar(View rootView, String message, String color) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT|Snackbar.ANIMATION_MODE_SLIDE);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.topMargin = 80;
        if (color.equals("red")) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), android.R.color.holo_red_dark));
        } else
            snackbarView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), android.R.color.holo_green_dark));
        snackbarView.setLayoutParams(params);

        snackbar.show();
        snackbar.show();
    }
}