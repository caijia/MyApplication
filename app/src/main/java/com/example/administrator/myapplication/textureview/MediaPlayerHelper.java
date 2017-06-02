package com.example.administrator.myapplication.textureview;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Surface;

import java.io.IOException;

import static android.media.MediaPlayer.MEDIA_ERROR_SERVER_DIED;

/**
 * Created by cai.jia on 2017/6/2 0002
 */

public class MediaPlayerHelper {

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private static volatile MediaPlayerHelper instance;
    private MediaPlayer mediaPlayer;

    private int currentState = STATE_IDLE;

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

    public void startPlay(@NonNull String url, @Nullable Surface surface, @Nullable final MediaCallback callback) {
        try {
            release();
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            DefaultMediaCallback defaultCb = new DefaultMediaCallback(callback);

            mediaPlayer.setDataSource(url);
            if (surface != null) {
                mediaPlayer.setSurface(surface);
            }

            if (callback != null) {
                mediaPlayer.setOnPreparedListener(defaultCb);
                mediaPlayer.setOnCompletionListener(defaultCb);
                mediaPlayer.setOnErrorListener(defaultCb);
                mediaPlayer.setOnBufferingUpdateListener(defaultCb);
                mediaPlayer.setOnInfoListener(defaultCb);
            }

            mediaPlayer.prepareAsync();
            currentState = STATE_PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void resume() {

    }

    public void pause() {

    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface MediaCallback extends MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
            MediaPlayer.OnPreparedListener {
    }

    private static class DefaultMediaCallback implements MediaCallback {
        private MediaCallback callback;

        public DefaultMediaCallback(MediaCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            callback.onBufferingUpdate(mp, percent);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            callback.onCompletion(mp);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (what == MEDIA_ERROR_SERVER_DIED) {
                mp.reset();
            }
            return callback.onError(mp, what, extra);
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return callback.onInfo(mp, what, extra);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            callback.onPrepared(mp);
        }
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
