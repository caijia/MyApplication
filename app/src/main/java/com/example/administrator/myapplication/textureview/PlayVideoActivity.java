package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;

public class PlayVideoActivity extends Activity implements TextureView.SurfaceTextureListener {
    private TextureView mTextureView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);

        setContentView(mTextureView);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}