package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by cai.jia on 2017/2/27 0027
 * 此类为了适配LinearLayout下TextView有时设置了Weight属性来达到最大宽度,但是当文字
 * 没有达到最大宽度时,右边会有空白,跟设置稿不服,(为了解决这个问题而存在)
 */

public class WightWrapTextView extends TextView {

    private int originWeightWidth;

    public WightWrapTextView(Context context) {
        this(context, null);
    }

    public WightWrapTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        System.out.println(toString());
    }

    public WightWrapTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (originWeightWidth != 0) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null && params instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) params;
                lp.weight = 0;
                lp.width = WRAP_CONTENT;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null && params instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) params;
            lp.weight = 0;
            lp.width = WRAP_CONTENT;
        }
        if (originWeightWidth == 0) {
            originWeightWidth = w;
            setMaxWidth(originWeightWidth);
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (originWeightWidth == 0) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (originWeightWidth != 0 && getVisibility() == VISIBLE) {
                        setMaxWidth(originWeightWidth);
                    }
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
        super.setText(text, type);
    }
}
