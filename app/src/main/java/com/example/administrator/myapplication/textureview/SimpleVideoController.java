package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.Locale;

/**
 * Created by cai.jia on 2017/7/2.
 */
public class SimpleVideoController extends RelativeLayout implements VideoView.Controller, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private TextView videoStartPauseTv;
    private FrameLayout videoStartPauseFl;
    private TextView videoVoiceTv;
    private TextView videoCurrentTimeTv;
    private SeekBar videoPlayProgressSeekBar;
    private TextView videoTotalTimeTv;
    private TextView videoFullScreenTv;
    private LinearLayout videoBottomBarLl;

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

        videoStartPauseFl.setOnClickListener(this);
        videoVoiceTv.setOnClickListener(this);
        videoFullScreenTv.setOnClickListener(this);
        videoPlayProgressSeekBar.setOnSeekBarChangeListener(this);
    }

    private VideoView videoView;

    @Override
    public void onAttach(VideoView view) {
        this.videoView = view;
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
    public void onPrepared() {
        videoView.start();
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

    @Override
    public void onBufferStart(int speed) {
        videoStartPauseTv.setSelected(false);
        Log.d("controller", "status:onBufferStart" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    @Override
    public void onBufferEnd(int speed) {
        videoStartPauseTv.setSelected(true);
        Log.d("controller", "status:onBufferEnd" + (videoStartPauseTv.isSelected() ? "start" : "pause"));
    }

    @Override
    public void onPlayProgress(long progress, long total) {
        videoPlayProgressSeekBar.setMax((int) total);
        videoPlayProgressSeekBar.setProgress((int) progress);
        videoCurrentTimeTv.setText(formatTime(progress));
        videoTotalTimeTv.setText(formatTime(total));
    }

    private String videoUrl;

    @Override
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    private String formatTime(long duration) {
        int totalSecond = (int) (duration / 1000) + (duration % 1000 < 500 ? 0 : 1);
        int h = totalSecond / 3600;
        int m = totalSecond % 3600 / 60;
        int s = totalSecond % 3600 % 60;
        if (h > 0) {
            return String.format(Locale.CHINESE,"%02d:%02d:%02d", h, m, s);

        }else{
            return String.format(Locale.CHINESE,"%02d:%02d", m, s);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == videoStartPauseFl) {
            boolean start = videoStartPauseTv.isSelected();
            if (start) {
                videoView.pause();

            }else{
                videoView.start(videoUrl);
            }

        } else if (view == videoVoiceTv) {


        } else if (view == videoFullScreenTv) {


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
