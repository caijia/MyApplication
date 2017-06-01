package com.example.administrator.myapplication.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import static android.support.v7.appcompat.R.attr.selectableItemBackgroundBorderless;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by cai.jia on 2017/5/22 0022
 */

public class NavigationHelper {

    private @ColorInt int defaultColor;

    private Toolbar toolbar;
    private Context context;
    private AppCompatActivity compatActivity;

    public NavigationHelper(AppCompatActivity compatActivity,Toolbar toolbar,boolean hasNav) {
        this.toolbar = toolbar;
        this.context = toolbar.getContext();
        this.compatActivity = compatActivity;
        defaultColor = Color.parseColor("#ff333333");
        if (hasNav) {
            setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_btn_colored_material);
        }
    }

    public void setNavigationIcon(@DrawableRes int icon) {
        toolbar.setNavigationIcon(icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compatActivity.onBackPressed();
            }
        });
    }

    public void addMenu(String text, int actionId, View.OnClickListener l) {
        addMenu(0, text, actionId, l);
    }

    public void addMenu(@DrawableRes int icon, int actionId, View.OnClickListener l) {
        addMenu(icon, "", actionId, l);
    }

    public void addMenu(@DrawableRes int icon, String text, int actionId, View.OnClickListener l) {
        int paddingRight = dpToPx(12);
        int paddingLeft = dpToPx(6);
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(defaultColor);
        textView.setTextSize(14);
        textView.setPadding(paddingLeft, 0, paddingRight, 0);
        ViewCompat.setBackground(textView,getSystemRippleDrawable());
        textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        textView.setId(actionId);
        textView.setOnClickListener(l);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        textView.setGravity(Gravity.CENTER);
        p.gravity = Gravity.END;
        toolbar.addView(textView, p);
    }

    private Drawable getSystemRippleDrawable() {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(selectableItemBackgroundBorderless, typedValue, true);
            int[] attribute = new int[]{selectableItemBackgroundBorderless};
            TypedArray typedArray = context.getTheme()
                    .obtainStyledAttributes(typedValue.resourceId, attribute);
            return typedArray.getDrawable(0);
        } catch (Exception e) {}
        return null;
    }

    public void setCenterTitle(String centerTitle) {
        setCenterTitle(centerTitle, 17, defaultColor);
    }

    public void setCenterTitle(String centerTitle, @ColorInt int textColor) {
        setCenterTitle(centerTitle, 17, textColor);
    }

    public void setCenterTitle(String centerTitle, int textSize, @ColorInt int textColor) {
        TextView textView = new TextView(context);
        textView.setText(centerTitle);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        p.gravity = Gravity.CENTER;
        toolbar.addView(textView, p);
    }

    private int dpToPx(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }
}
