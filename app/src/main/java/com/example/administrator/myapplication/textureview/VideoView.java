package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by cai.jia on 2017/6/30 0030
 */

public class VideoView extends TextureView implements TextureView.SurfaceTextureListener,
        OnPlayMediaListener {

    private MediaPlayerHelper playerHelper;
    private int videoWidth;
    private int videoHeight;
    private Surface surface;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs,
                     @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        playerHelper = new MediaPlayerHelper();
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
        adjustAspectRatio(videoWidth, videoHeight);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        pause();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void setVideoUrl(String url) {
        if (playerHelper != null) {
            playerHelper.setOnPlayMediaListener(this);
            playerHelper.setDataSource(url, surface);
            prepareAsync();
        }
    }

    private void prepareAsync() {
        if (playerHelper != null) {
            playerHelper.prepareAsync();
        }
    }

    public void start() {
        if (playerHelper != null) {
            playerHelper.start();
        }
    }

    public void resume() {
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

    private OnPlayMediaListener callback;

    public void setOnPlayMediaListener(OnPlayMediaListener callback) {
        this.callback = callback;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (callback != null) {
            callback.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (callback != null) {
            callback.onCompletion(mp);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onError(mp,what,extra);
        }
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onInfo(mp,what,extra);
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        start();
        if (callback != null) {
            callback.onPrepared(mp);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        videoWidth = width;
        videoHeight = height;
        adjustAspectRatio(videoWidth, videoHeight);
        if (callback != null) {
            callback.onVideoSizeChanged(mp,width,height);
        }
    }

    @Override
    public void onPlayMediaProgress(long duration, long currentPosition) {
        if (callback != null) {
            callback.onPlayMediaProgress(duration,currentPosition);
        }
    }

    private void adjustAspectRatio(int videoWidth, int videoHeight) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        double aspectRatio = (double) videoHeight / videoWidth;

        int newWidth, newHeight;
        if (viewHeight > (int) (viewWidth * aspectRatio)) {
            newWidth = viewWidth;
            newHeight = (int) (viewWidth * aspectRatio);

        } else {
            newWidth = (int) (viewHeight / aspectRatio);
            newHeight = viewHeight;
        }
        int xOffset = (viewWidth - newWidth) / 2;
        int yOffset = (viewHeight - newHeight) / 2;

        Matrix transform = new Matrix();
        transform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
        transform.postTranslate(xOffset, yOffset);
        setTransform(transform);
    }
}
