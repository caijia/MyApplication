package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by cai.jia on 2017/6/30 0030
 */

public class VideoView extends TextureView implements TextureView.SurfaceTextureListener,
        OnPlayMediaListener {

    private static final int WRAP_CONTENT = 1;
    private static final int CENTER_CROP = 2;

    private MediaPlayerHelper playerHelper;
    private int videoWidth;
    private int videoHeight;
    private Surface surface;
    private OnPlayMediaListener callback;

    private int scaleType = CENTER_CROP;
    private int rotation;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        playerHelper = new MediaPlayerHelper();
        playerHelper.setOnPlayMediaListener(this);
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (surface == null) {
            surface = new Surface(surfaceTexture);

        } else {
            surface = new Surface(surfaceTexture);
            setSurface(surface);
            resume();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        setVideoScaleType();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        pause();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void start(String url) {
        if (playerHelper != null) {
            playerHelper.start(url, surface);
        }
    }

    private void resume() {
        if (playerHelper != null) {
            playerHelper.resume();
        }
    }

    public void destroy() {
        if (playerHelper != null) {
            playerHelper.stopPlayback();
        }
    }

    public void pause() {
        if (playerHelper != null) {
            playerHelper.pause();
        }
    }

    private void setSurface(Surface surface) {
        if (playerHelper != null) {
            playerHelper.setSurface(surface);
        }
    }

    public void seekTo(int milliSeconds) {
        if (playerHelper != null) {
            playerHelper.seekTo(milliSeconds);
        }
    }

    public void relativeSeekTo(int milliSeconds) {
        if (playerHelper != null) {
            playerHelper.relativeSeekTo(milliSeconds);
        }
    }

    public void setOnPlayMediaListener(OnPlayMediaListener callback) {
        this.callback = callback;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (callback != null) {
            callback.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        if (callback != null) {
            callback.onCompletion(mp);
        }
    }

    @Override
    public void onStart() {
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    public void onPause() {
        if (callback != null) {
            callback.onPause();
        }
    }

    @Override
    public void onPreparing() {
        if (callback != null) {
            callback.onPreparing();
        }
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onError(mp, what, extra);
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onInfo(mp, what, extra);
        }

        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED: {
                rotation = extra;
                break;
            }
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        if (callback != null) {
            callback.onPrepared(mp);
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height) {
        videoWidth = width;
        videoHeight = height;
        setVideoScaleType();
        if (callback != null) {
            callback.onVideoSizeChanged(mp, width, height);
        }
    }

    private void setVideoScaleType() {
        switch (scaleType) {
            case CENTER_CROP: {
                TextureTransformHelper.centerCrop(this, videoWidth, videoHeight, rotation);
                break;
            }

            case WRAP_CONTENT: {
                TextureTransformHelper.wrapContent(this, videoWidth, videoHeight, rotation);
                break;
            }
        }
    }

    @Override
    public void onPlayMediaProgress(long duration, long currentPosition) {
        if (callback != null) {
            callback.onPlayMediaProgress(duration, currentPosition);
        }
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }
}
