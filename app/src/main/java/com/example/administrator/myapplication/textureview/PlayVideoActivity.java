package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.util.ToastManager;

public class PlayVideoActivity extends Activity implements TextureView.SurfaceTextureListener {
    private TextureView mTextureView;
    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        mTextureView = (TextureView) findViewById(R.id.texture_view);
        mTextureView.setSurfaceTextureListener(this);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        MediaPlayerHelper.getInstance().startPlay(url,surface,new MediaPlayerHelper.SimpleMediaCallback(){

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
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
}