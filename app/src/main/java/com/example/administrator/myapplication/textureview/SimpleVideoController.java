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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
    private ViewSwitcher viewSwitcher;

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
        viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);

        videoStartPauseFl.setOnClickListener(this);
        videoVoiceTv.setOnClickListener(this);
        videoFullScreenTv.setOnClickListener(this);
        videoPlayProgressSeekBar.setOnSeekBarChangeListener(this);
        setOnClickListener(this);
    }

    private VideoView videoView;

    private int currentBrightness;

    public void onLeftVerticalMove(@MoveState int state,float distance) {
        int dp = ControllerUtil.spToDp(getContext(), distance);
        int brightness = dp * 2;
        switch (state) {
            case GestureVideoController.START:{
                viewSwitcher.setVisibility(VISIBLE);
                viewSwitcher.setDisplayedChild(0);
                currentBrightness = ControllerUtil.getActivityBrightness(getContext());
                ControllerUtil.setActivityBrightness(getContext(), currentBrightness + brightness);
                break;
            }

            case GestureVideoController.MOVE:{
                ControllerUtil.setActivityBrightness(getContext(), currentBrightness + brightness);
                break;
            }

            case GestureVideoController.END:{
                viewSwitcher.setVisibility(GONE);
                ControllerUtil.setActivityBrightness(getContext(), currentBrightness + brightness);
                break;
            }
        }
    }

    private int currentVolume;

    public void onRightVerticalMove(@MoveState int state,float distance) {
        int dp = ControllerUtil.spToDp(getContext(), distance);
        int volume = (int) (dp * 0.1f);
        switch (state) {
            case GestureVideoController.START:{
                viewSwitcher.setVisibility(VISIBLE);
                viewSwitcher.setDisplayedChild(1);
                currentVolume = ControllerUtil.getVolume(getContext());
                ControllerUtil.setVolume(getContext(), currentVolume + volume);
                break;
            }

            case GestureVideoController.MOVE:{
                ControllerUtil.setVolume(getContext(), currentVolume + volume);
                break;
            }

            case GestureVideoController.END:{
                viewSwitcher.setVisibility(GONE);
                ControllerUtil.setVolume(getContext(), currentVolume + volume);
                break;
            }
        }
    }

    private int currentProgress;
    private boolean horizontalMove;

    public void onHorizontalMove(@MoveState int state,float distance) {
        int dp = ControllerUtil.spToDp(getContext(), distance);
        int time = Math.round(dp * 0.5f) * 1000;
        switch (state) {
            case GestureVideoController.START:{
                horizontalMove = true;
                currentProgress = videoPlayProgressSeekBar.getProgress();
                videoPlayProgressSeekBar.setProgress(currentProgress + time);
                break;
            }

            case GestureVideoController.MOVE:{
                videoPlayProgressSeekBar.setProgress(currentProgress + time);
                break;
            }

            case GestureVideoController.END:{
                horizontalMove = false;
                videoPlayProgressSeekBar.setProgress(currentProgress + time);
                videoView.seekTo(videoPlayProgressSeekBar.getProgress());
                break;
            }
        }
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
    }

    @Override
    public void onPause() {
        videoStartPauseTv.setSelected(false);
    }

    @Override
    public void onCompletion() {
        videoStartPauseTv.setSelected(false);
    }

    @Override
    public void onError() {
        videoStartPauseTv.setSelected(false);
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
        if (!horizontalMove) {
            videoPlayProgressSeekBar.setProgress((int) progress);
        }
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
        videoCurrentTimeTv.setText(ControllerUtil.formatTime(progress));
        videoTotalTimeTv.setText(ControllerUtil.formatTime(seekBar.getMax()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoView.seekTo(seekBar.getProgress());
    }
}
