package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Created by cai.jia on 2017/7/4 0004
 */

public class ControllerUtil {

    public static @Nullable Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static String formatTime(long duration) {
        int totalSecond = (int) (duration / 1000) + (duration % 1000 < 500 ? 0 : 1);
        int h = totalSecond / 3600;
        int m = totalSecond % 3600 / 60;
        int s = totalSecond % 3600 % 60;
        if (h > 0) {
            return String.format(Locale.CHINESE,"%02d:%02d:%02d", h, m, s);

        }else{
            return String.format(Locale.CHINESE,"%02d:%02d", m, s);
        }
    }

    public static void toggleActionBarAndStatusBar(Context context,boolean fullScreen) {
        Activity activity = getActivity(context);
        if (activity == null) {
            return;
        }

        Window window = activity.getWindow();
        if (window == null) {
            return;
        }

        ActionBar supportActionBar = null;
        android.app.ActionBar actionBar = null;
        if (activity instanceof AppCompatActivity) {
            supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();

        } else if (activity instanceof FragmentActivity) {
            actionBar = activity.getActionBar();
        }

        if (fullScreen) {
            if (supportActionBar != null) {
                supportActionBar.hide();
            }

            if (actionBar != null) {
                actionBar.hide();
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            if (supportActionBar != null) {
                supportActionBar.show();
            }

            if (actionBar != null) {
                actionBar.show();
            }

            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
