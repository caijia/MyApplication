package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/7/2.
 */
public class SimpleVideoController extends FrameLayout implements VideoView.Controller {

    public SimpleVideoController(@NonNull Context context) {
        this(context,null);
    }

    public SimpleVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleVideoController(@NonNull Context context, @Nullable AttributeSet attrs,
                                 @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleVideoController(@NonNull Context context, @Nullable AttributeSet attrs,
                                 @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_controller,this,true);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onAttach(VideoView view) {

    }

    @Override
    public void onLeftVerticalMove(float distance) {

    }

    @Override
    public void onRightVerticalMove(float distance) {

    }

    @Override
    public void onHorizontalMove(float distance) {

    }

    @Override
    public void onPreparing() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onBufferStart(int speed) {

    }

    @Override
    public void onBufferEnd(int speed) {

    }
}
