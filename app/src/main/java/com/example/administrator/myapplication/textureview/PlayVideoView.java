package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by cai.jia on 2017/7/2.
 */

public class PlayVideoView extends FrameLayout {

    public PlayVideoView(@NonNull Context context) {
        this(context,null);
    }

    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                         @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayVideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                         @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private Controller controller;
    private VideoView videoView;

    private void init(Context context, AttributeSet attrs) {
        LayoutParams p = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        FrameLayout container = new FrameLayout(context);
        addView(container, p);

        videoView = new VideoView(context);
        container.addView(videoView, p);

        controller = new SimpleVideoController(context);
        container.addView((View) controller, p);
        controller.setParentLayout(this,container);

        VideoControllerHelper helper = new VideoControllerHelper(controller);
        helper.attachVideoView(videoView);
    }

    public void setVideoUrl(String videoUrl) {
        controller.setVideoUrl(videoUrl);
    }

    public void start(String videoUrl) {
        videoView.start(videoUrl);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (videoView != null) {
            videoView.destroy();
        }
        super.onDetachedFromWindow();
    }
}
