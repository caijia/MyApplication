package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.Locale;

public class PlayVideoActivity extends Activity implements
        OnPlayMediaListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
//    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
    private String url = "http://jetsunfile.cn-gd.ufileos.com/upload_29048693_868026024793198_1498553210";

    private Button startBtn;
    private Button resumtBtn;
    private Button pauseBtn;
    private Button seekBtn;
    private MediaPlayerHelper mediaPlayerHelper;

    private SeekBar playProgressBar;
    private TextView currentPlayTimeTv;
    private TextView totalPlayTimeTv;
    private TextView fullScreenTv;
    private FrameLayout textureParentFl;
    private VideoView jVideoView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        mediaPlayerHelper = new MediaPlayerHelper();

        jVideoView = (VideoView) findViewById(R.id.texture_view);
        startBtn = (Button) findViewById(R.id.start_btn);
        resumtBtn = (Button) findViewById(R.id.resume_btn);
        pauseBtn = (Button) findViewById(R.id.pause_btn);
        seekBtn = (Button) findViewById(R.id.seek_btn);

        playProgressBar = (SeekBar) findViewById(R.id.seekBar);
        currentPlayTimeTv = (TextView) findViewById(R.id.current_time_tv);
        totalPlayTimeTv = (TextView) findViewById(R.id.total_time_tv);
        playProgressBar.setOnSeekBarChangeListener(this);

        fullScreenTv = (TextView) findViewById(R.id.full_screen);
        textureParentFl = (FrameLayout) findViewById(R.id.texture_parent_fl);
        fullScreenTv.setOnClickListener(this);

        startBtn.setOnClickListener(this);
        resumtBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        seekBtn.setOnClickListener(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("error"+what);
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        jVideoView.start();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        System.out.println("width:" + width + "--height:" + height);
//        jVideoView.setmVideoWidthAndHeight(width,height);
    }

    @Override
    public void onPlayMediaProgress(long total, long progress) {
        System.out.println("total:" + total + "--progress:" + progress);
        totalPlayTimeTv.setText(toTime(total));
        currentPlayTimeTv.setText(toTime(progress));
        playProgressBar.setMax((int) total);
        playProgressBar.setProgress((int) progress);
    }

    private String toTime(long duration) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:{
                jVideoView.setOnPlayMediaListener(this);
                jVideoView.setVideoUrl(url);
                break;
            }

            case R.id.resume_btn:{
                jVideoView.start();
                break;
            }

            case R.id.pause_btn:{
                jVideoView.pause();
                break;
            }

            case R.id.seek_btn:{
                jVideoView.relativeSeekTo(10000);
                break;
            }

            case R.id.full_screen:{
                ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
                textureParentFl.removeView(jVideoView);

                ViewGroup.LayoutParams p = jVideoView.getLayoutParams();
                p.width = ViewGroup.LayoutParams.MATCH_PARENT;
                p.height = ViewGroup.LayoutParams.MATCH_PARENT;
                contentView.addView(jVideoView, p);

                //设置成横屏模式
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isFullScreen = true;
                break;
            }
        }
    }

    private boolean isFullScreen;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        jVideoView.destroy();
        super.onDestroy();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        jVideoView.seekTo(progress);
    }

    @Override
    public void onBackPressed() {
        if (isFullScreen) {
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            contentView.removeView(jVideoView);

            ViewGroup.LayoutParams p = jVideoView.getLayoutParams();
            p.width = ViewGroup.LayoutParams.MATCH_PARENT;
            p.height = ViewGroup.LayoutParams.MATCH_PARENT;
            textureParentFl.addView(jVideoView, p);

            //设置成竖屏模式
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isFullScreen = false;

        } else {
            super.onBackPressed();
        }
    }
}