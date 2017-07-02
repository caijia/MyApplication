package com.example.administrator.myapplication.textureview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewConfiguration;

import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END;
import static android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START;

/**
 * Created by cai.jia on 2017/6/30 0030
 */

public class VideoView extends TextureView implements TextureView.SurfaceTextureListener,
        OnPlayMediaListener {

    private static final int NONE = 0;
    private static final int HORIZONTAL = 1;
    private static final int VERTICAL_LEFT = 2;
    private static final int VERTICAL_RIGHT = 3;

    private static final int WRAP_CONTENT = 1;
    private static final int CENTER_CROP = 2;

    private MediaPlayerHelper playerHelper;
    private int videoWidth;
    private int videoHeight;
    private Surface surface;
    private int touchSlop;
    private OnPlayMediaListener callback;

    private float initialX;
    private float initialY;
    private float startX;
    private float startY;
    private int orientation = NONE;
    private Controller controller;
    private int scaleType = WRAP_CONTENT;

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
        ViewConfiguration config = ViewConfiguration.get(context);
        touchSlop = config.getScaledTouchSlop();
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

        if (controller != null) {
            controller.onPreparing();
        }
    }

    public void start() {
        if (playerHelper != null) {
            playerHelper.start();
        }

        if (controller != null) {
            controller.onStart();
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

        if (controller != null) {
            controller.onPause();
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

        if (controller != null) {
            controller.onCompletion();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onError(mp, what, extra);
        }

        if (controller != null) {
            controller.onError();
        }
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (callback != null) {
            callback.onInfo(mp, what, extra);
        }

        switch (what) {
            case MEDIA_INFO_BUFFERING_START: {
                if (controller != null) {
                    controller.onBufferStart(extra);
                }
                break;
            }

            case MEDIA_INFO_BUFFERING_END: {
                if (controller != null) {
                    controller.onBufferEnd(extra);
                }
                break;
            }
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
        setVideoScaleType();
        if (callback != null) {
            callback.onVideoSizeChanged(mp, width, height);
        }
    }

    private void setVideoScaleType() {
        switch (scaleType) {
            case CENTER_CROP: {
                TextureTransformHelper.centerCrop(this, videoWidth, videoHeight);
                break;
            }

            case WRAP_CONTENT: {
                TextureTransformHelper.wrapContent(this, videoWidth, videoHeight);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                initialX = x;
                initialY = y;
                startX = x;
                startY = y;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float deltaX = x - startX;
                float deltaY = y - startY;

                float distanceX = x - initialX;
                float distanceY = y - initialY;

                if (orientation == NONE && Math.abs(distanceX) > Math.abs(distanceY)
                        && Math.abs(distanceX) > touchSlop) {
                    orientation = HORIZONTAL;
                }

                if (orientation == NONE && Math.abs(distanceY) > Math.abs(distanceX)
                        && Math.abs(distanceY) > touchSlop) {
                    boolean left = x < getWidth() / 2;
                    orientation = left ? VERTICAL_LEFT : VERTICAL_RIGHT;
                }

                startX = x;
                startY = y;

                if (orientation == NONE || controller == null) {
                    return false;
                }
                switch (orientation) {
                    case HORIZONTAL: {
                        controller.onHorizontalMove(deltaX);
                        break;
                    }

                    case VERTICAL_LEFT: {
                        controller.onLeftVerticalMove(deltaY);
                        break;
                    }

                    case VERTICAL_RIGHT: {
                        controller.onRightVerticalMove(deltaY);
                        break;
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                initialX = 0;
                initialY = 0;
                startX = 0;
                startY = 0;
                orientation = NONE;
                break;
            }
        }
        return true;
    }

    public void setPlayController(Controller controller) {
        this.controller = controller;
        if (controller != null) {
            controller.onAttach(this);
        }
    }

    public void setScaleType(int scaleType) {
        this.scaleType = scaleType;
    }

    public interface Controller {

        void onAttach(VideoView view);

        void onLeftVerticalMove(float distance);

        void onRightVerticalMove(float distance);

        void onHorizontalMove(float distance);

        void onPreparing();

        void onStart();

        void onPause();

        void onCompletion();

        void onError();

        void onBufferStart(int speed);

        void onBufferEnd(int speed);

    }
}
