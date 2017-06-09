package com.example.administrator.myapplication.textureview;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;

import java.io.IOException;

/**
 * Created by cai.jia on 2017/6/2 0002
 */

public class MediaPlayerHelper implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private static volatile MediaPlayerHelper instance;
    private MediaPlayer mediaPlayer;
    private MediaCallback callback;

    private int currentState = STATE_IDLE;
    private int currentPosition;

    private MediaPlayerHelper() {

    }

    public static MediaPlayerHelper getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerHelper.class) {
                if (instance == null) {
                    instance = new MediaPlayerHelper();
                }
            }
        }
        return instance;
    }

    public void startPlay(@NonNull String url, @Nullable MediaCallback callback) {
        startPlay(url, null, callback);
    }

    public void startPlay(@NonNull String url, @Nullable Surface surface,
                          @Nullable final MediaCallback callback) {
        this.callback = callback;
        try {
            release();
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.setDataSource(url);
            if (surface != null) {
                mediaPlayer.setSurface(surface);
            }

            if (callback != null) {
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnInfoListener(this);
            }

            mediaPlayer.prepareAsync();
            currentState = STATE_PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        callback = null;
        currentPosition = 0;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = STATE_IDLE;
        }
    }

    public void resume() {
        if (isInPlaybackState()) {
            if (!mediaPlayer.isPlaying() && currentState == STATE_PAUSED) {
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
                currentState = STATE_PLAYING;
            }
        }
    }

    public void pause() {
        if (isInPlaybackState()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentPosition = mediaPlayer.getCurrentPosition();
                currentState = STATE_PAUSED;
            }
        }
    }

    public void seekTo(int currentPosition) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
    }

    public void relativeSeekTo(int msec) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(currentPosition + msec);
            mediaPlayer.start();
        }
    }

    public boolean isInPlaybackState() {
        return (mediaPlayer != null &&
                currentState != STATE_ERROR &&
                currentState != STATE_IDLE &&
                currentState != STATE_PREPARING);
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        currentState = STATE_PREPARED;
        mp.seekTo(currentPosition);
        mp.start();
        currentState = STATE_PLAYING;
        if (callback != null) {
            callback.onPrepared(mp);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentState = STATE_PLAYBACK_COMPLETED;
        if (callback != null) {
            callback.onCompletion(mp);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        currentState = STATE_ERROR;
        if (callback != null) {
            return callback.onError(mp, what, extra);
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (callback != null) {
            callback.onBufferingUpdate(mp, percent);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (callback != null) {
            return callback.onInfo(mp, what, extra);
        }
        return false;
    }

    public interface MediaCallback extends MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
            MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener {
    }

    public static class SimpleMediaCallback implements MediaCallback {

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
    }
}
