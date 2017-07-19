package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;

public class MultiColorTextView extends AppCompatTextView {

    public MultiColorTextView(Context context) {
        this(context,null);
    }

    public MultiColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MultiColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private TextPaint paint;
    private Rect drawRect = new Rect();

    private void init(Context context, AttributeSet attrs) {
        paint = getPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.getTextBounds(getText().toString(),0,getText().toString().length(),drawRect);
        int bottom = (getHeight() - drawRect.height()) / 2 + drawRect.height();
        canvas.drawText(getText().toString(), drawRect.left, bottom, paint);

    }
}