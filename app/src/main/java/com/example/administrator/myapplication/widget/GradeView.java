package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.myapplication.R;

/**
 * 用户积分等级View
 * Created by cai.jia on 2017/1/6 0006
 */

public class GradeView extends View {

    public GradeView(Context context) {
        this(context,null);
    }

    public GradeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_camera);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
    }

    Paint paint = new Paint();
    private Bitmap bitmap;
    int bitmapWidth;
    int bitmapHeight;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,0,0,paint);
        canvas.drawRect(0, 0, 60, 60, paint);

    }


    /**
     * 几个等级
     */
    private int gradeCount;




}
