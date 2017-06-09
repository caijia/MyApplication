package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.R;

public class PlayVideoActivity extends Activity implements TextureView.SurfaceTextureListener, MediaPlayerHelper.MediaCallback, View.OnClickListener {
    private TextureView mTextureView;
    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";

    private Button startBtn;
    private Button stopBtn;
    private Button resumtBtn;
    private Button pauseBtn;
    private Button seekBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        mTextureView = (TextureView) findViewById(R.id.texture_view);
        startBtn = (Button) findViewById(R.id.start_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);
        resumtBtn = (Button) findViewById(R.id.resume_btn);
        pauseBtn = (Button) findViewById(R.id.pause_btn);
        seekBtn = (Button) findViewById(R.id.seek_btn);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        resumtBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        seekBtn.setOnClickListener(this);
        mTextureView.setSurfaceTextureListener(this);
    }

    private Surface surface;

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        System.out.println("onSurfaceTextureAvailable");
        surface = new Surface(surfaceTexture);
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerHelper.getInstance().stopPlay();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:{
                MediaPlayerHelper.getInstance().startPlay(url,surface,this);
                break;
            }

            case R.id.stop_btn:{
                MediaPlayerHelper.getInstance().stopPlay();
                break;
            }

            case R.id.resume_btn:{
                MediaPlayerHelper.getInstance().resume();
                break;
            }

            case R.id.pause_btn:{
                MediaPlayerHelper.getInstance().pause();
                break;
            }

            case R.id.seek_btn:{
                MediaPlayerHelper.getInstance().seekTo(20);
                break;
            }
        }
    }
}