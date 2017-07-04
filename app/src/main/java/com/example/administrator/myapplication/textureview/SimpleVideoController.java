package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.text.MessageFormat;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by cai.jia on 2017/7/2.
 */
public class SimpleVideoController extends GestureVideoController implements Controller, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView videoStartPauseTv;
    private FrameLayout videoStartPauseFl;
    private TextView videoVoiceTv;
    private TextView videoCurrentTimeTv;
    private SeekBar videoPlayProgressSeekBar;
    private TextView videoTotalTimeTv;
    private TextView videoFullScreenTv;
    private LinearLayout videoBottomBarLl;
    private FrameLayout videoProgressFl;
    private TextView videoNetSpeedTv;

    public SimpleVideoController(@NonNull Context context) {
        this(context, null);
    }

    public SimpleVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
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
        LayoutInflater.from(context).inflate(R.layout.video_controller, this, true);
        videoStartPauseTv = (TextView) findViewById(R.id.video_start_pause_tv);
        videoStartPauseFl = (FrameLayout) findViewById(R.id.video_start_pause_fl);
        videoVoiceTv = (TextView) findViewById(R.id.video_voice_tv);
        videoCurrentTimeTv = (TextView) findViewById(R.id.video_current_time_tv);
        videoPlayProgressSeekBar = (SeekBar) findViewById(R.id.video_play_progress_seek_bar);
        videoTotalTimeTv = (TextView) findViewById(R.id.video_total_time_tv);
        videoFullScreenTv = (TextView) findViewById(R.id.video_full_screen_tv);
        videoBottomBarLl = (LinearLayout) findViewById(R.id.video_bottom_bar_ll);
        videoProgressFl = (FrameLayout) findViewById(R.id.video_progress_fl);
        videoNetSpeedTv = (TextView) findViewById(R.id.video_net_speed_tv);

        videoStartPauseFl.setOnClickListener(this);
        videoVoiceTv.setOnClickListener(this);
        videoFullScreenTv.setOnClickListener(this);
        videoPlayProgressSeekBar.setOnSeekBarChangeListener(this);
        setOnClickListener(this);
    }

    private VideoView videoView;

    public void onLeftVerticalMove(float distance) {
        Log.d("controller", "onLeftVerticalMove:" + distance);
    }

    public void onRightVerticalMove(float distance) {
        Log.d("controller", "onRightVerticalMove:" + distance);
    }

    public void onHorizontalMove(float distance) {
        Log.d("controller", "onHorizontalMove:" + distance);
    }

    @Override
    public void onPreparing() {
        videoProgressFl.setVisibility(VISIBLE);
    }

    @Override
    public void onPrepared() {
        videoProgressFl.setVisibility(GONE);
        videoView.start(videoUrl);
    }

    @Override
    public void onStart() {
        videoStartPauseTv.setSelected(true);
        Log.d("controller", "status:onStart" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    @Override
    public void onPause() {
        videoStartPauseTv.setSelected(false);
        Log.d("controller", "status:onPause" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    @Override
    public void onCompletion() {
        videoStartPauseTv.setSelected(false);
        Log.d("controller", "status:onCompletion" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    @Override
    public void onError() {
        videoStartPauseTv.setSelected(false);
        Log.d("controller", "status:onError" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    private boolean isPlaying;

    @Override
    public void onBufferStart(int speed) {
        isPlaying = videoStartPauseTv.isSelected();
        videoStartPauseTv.setSelected(false);
        videoProgressFl.setVisibility(VISIBLE);
        videoNetSpeedTv.setText(MessageFormat.format("{0}k/s", speed));
    }

    @Override
    public void onBufferEnd(int speed) {
        videoProgressFl.setVisibility(GONE);
        videoStartPauseTv.setSelected(isPlaying);
        if (isPlaying) {
            videoView.start(videoUrl);

        }else{
            videoView.pause();
        }
    }

    @Override
    public void onPlayProgress(long progress, long total) {
        videoPlayProgressSeekBar.setMax((int) total);
        videoPlayProgressSeekBar.setProgress((int) progress);
        videoCurrentTimeTv.setText(ControllerUtil.formatTime(progress));
        videoTotalTimeTv.setText(ControllerUtil.formatTime(total));
    }

    @Override
    public void onBufferingUpdate(int percent) {
        int secondaryProgress = Math.round(videoPlayProgressSeekBar.getMax() * percent * 0.01f);
        videoPlayProgressSeekBar.setSecondaryProgress(secondaryProgress);
    }

    @Override
    public void attachVideoView(VideoView view) {
        videoView = view;
    }

    @Override
    public void showController() {
        videoBottomBarLl.setVisibility(VISIBLE);
    }

    @Override
    public void hideController() {
        videoBottomBarLl.setVisibility(GONE);
    }

    private String videoUrl;
    private ViewGroup videoContainerParent;
    private ViewGroup videoContainer;

    @Override
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public void setParentLayout(ViewGroup parent,ViewGroup container) {
        this.videoContainerParent = parent;
        this.videoContainer = container;
    }

    @Override
    public void onClick(View view) {
        if (view == videoStartPauseFl) {
            boolean isPlaying = videoStartPauseTv.isSelected();
            if (isPlaying) {
                videoView.pause();

            }else{
                videoView.start(videoUrl);
            }

        } else if (view == videoVoiceTv) {


        } else if (view == videoFullScreenTv) {
            toggleFullScreen();

        } else if (view == this) {
            isShowController = !isShowController;
            if (isShowController) {
                hideController();
            }else{
                showController();
            }
        }
    }

    private boolean isShowController;

    private void toggleFullScreen() {
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
            ControllerUtil.toggleActionBarAndStatusBar(getContext(),true);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }else{
            content.removeView(videoContainer);
            videoContainerParent.addView(videoContainer, MATCH_PARENT, MATCH_PARENT);
            ControllerUtil.toggleActionBarAndStatusBar(getContext(),false);
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoView.seekTo(seekBar.getProgress());
    }
}
