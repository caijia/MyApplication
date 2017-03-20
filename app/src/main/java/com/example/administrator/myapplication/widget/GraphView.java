package com.example.administrator.myapplication.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by cai.jia on 2017/3/20 0020
 */

public class GraphView extends LinearLayout {


    public GraphView(@NonNull Context context) {
        this(context, null);
    }

    public GraphView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GraphView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private GraphViewY graphViewY;
    private GraphViewX graphViewX;

    private void init(Context context) {
        setOrientation(HORIZONTAL);

        // add GraphViewY
        graphViewY = new GraphViewY(getContext());
        addView(graphViewY);

        // add GraphViewX
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
        graphViewX = new GraphViewX(getContext());
        horizontalScrollView.addView(graphViewX);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        addView(horizontalScrollView);
    }

    public void addLineSeries(LineDataSeries lineDataSeries) {
        if (lineDataSeries == null) {
            return;
        }
        graphViewX.addLineSeries(lineDataSeries);
    }

    public void setViewPort(ViewPort viewPort) {
        if (viewPort != null) {
            graphViewY.setViewPort(viewPort);
            graphViewX.setViewPort(viewPort);
        }
    }
}
