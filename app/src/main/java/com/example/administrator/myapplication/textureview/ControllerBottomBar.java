package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by cai.jia on 2017/7/6 0006
 */

public class ControllerBottomBar extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView voiceTv;
    private TextView currentTimeTv;
    private SeekBar progressSeekBar;
    private TextView totalTimeTv;
    private TextView fullScreenTv;
    private ViewGroup videoContainerParent;
    private ViewGroup videoContainer;
    private int currentVolume;

    public ControllerBottomBar(Context context) {
        this(context, null);
    }

    public ControllerBottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControllerBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ControllerBottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.controller_bottom_bar, this, true);
        setOrientation(VERTICAL);
        voiceTv = (TextView) findViewById(R.id.video_voice_tv);
        currentTimeTv = (TextView) findViewById(R.id.video_current_time_tv);
        progressSeekBar = (SeekBar) findViewById(R.id.video_play_progress_seek_bar);
        totalTimeTv = (TextView) findViewById(R.id.video_total_time_tv);
        fullScreenTv = (TextView) findViewById(R.id.video_full_screen_tv);

        voiceTv.setOnClickListener(this);
        fullScreenTv.setOnClickListener(this);
        progressSeekBar.setOnSeekBarChangeListener(this);
        setCurrentVolume(ControllerUtil.getVolume(context));
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public void setVisibility(int visibility) {
        if (!videoPrepared) {
            return;
        }
        super.setVisibility(visibility);
    }

    private boolean videoPrepared;

    public void setVideoPrepared(boolean videoPrepared) {
        this.videoPrepared = videoPrepared;
    }

    @Override
    public void onClick(View v) {
        if (v == voiceTv) {
            v.setSelected(!v.isSelected());
            boolean hasVolume = v.isSelected();
            ControllerUtil.setVolume(getContext(), hasVolume ? currentVolume : 0);

        } else if (v == fullScreenTv) {
            toggleFullScreen();
        }
    }

    public void setFullScreenLayout(ViewGroup videoContainerParent, ViewGroup videoContainer) {
        this.videoContainer = videoContainer;
        this.videoContainerParent = videoContainerParent;
    }

    private void toggleFullScreen() {
        if (!videoPrepared) {
            return;
        }
        if (videoContainerParent == null || videoContainer == null) {
            return;
        }

        int index = videoContainerParent.indexOfChild(videoContainer);
        boolean notFullScreen = index != -1;
        Activity activity = ControllerUtil.getActivity(getContext());
        if (activity == null) {
            return;
        }

        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        if (notFullScreen) {
            videoContainerParent.removeView(videoContainer);
            content.addView(videoContainer, MATCH_PARENT, MATCH_PARENT);
            ControllerUtil.toggleActionBarAndStatusBar(getContext(), true);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            content.removeView(videoContainer);
            videoContainerParent.addView(videoContainer, MATCH_PARENT, MATCH_PARENT);
            ControllerUtil.toggleActionBarAndStatusBar(getContext(), false);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume = currentVolume;
        boolean hasVolume = currentVolume > 0;
        voiceTv.setSelected(hasVolume);
    }

    public void setProgress(long progress) {
        if (!videoPrepared) {
            return;
        }
        String currentTime = ControllerUtil.formatTime(progress);
        currentTimeTv.setText(currentTime);
        progressSeekBar.setProgress((int) progress);
    }

    public void setMax(long max) {
        if (!videoPrepared) {
            return;
        }
        String totalTime = ControllerUtil.formatTime(max);
        totalTimeTv.setText(totalTime);
        progressSeekBar.setMax((int) max);
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (!videoPrepared) {
            return;
        }
        progressSeekBar.setSecondaryProgress(secondaryProgress);
    }

    public int getMax() {
        return progressSeekBar.getMax();
    }

    public int getProgress() {
        return progressSeekBar.getProgress();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        currentTimeTv.setText(ControllerUtil.formatTime(progress));
        totalTimeTv.setText(ControllerUtil.formatTime(seekBar.getMax()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (onPlayProgressChangeListener != null) {
            onPlayProgressChangeListener.onPlayProgressChange(seekBar.getProgress());
        }
    }

    public interface OnPlayProgressChangeListener{

        void onPlayProgressChange(int progress);
    }

    private OnPlayProgressChangeListener onPlayProgressChangeListener;

    public void setOnPlayProgressChangeListener(OnPlayProgressChangeListener l) {
        this.onPlayProgressChangeListener = l;
    }
}
