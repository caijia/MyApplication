package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by cai.jia on 2017/7/2.
 */

public class PlayVideoView extends FrameLayout {

    public PlayVideoView(@NonNull Context context) {
        super(context);
    }

    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                         @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                         @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
