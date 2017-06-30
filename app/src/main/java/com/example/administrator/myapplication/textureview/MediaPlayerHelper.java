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
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener,
        MediaProgressHelper.OnPlayMediaProgressListener, MediaPlayer.OnVideoSizeChangedListener {

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private MediaPlayer mediaPlayer;
    private OnPlayMediaListener callback;
    private MediaProgressHelper progressHelper;

    private int currentState = STATE_IDLE;

    public void setOnPlayMediaListener(OnPlayMediaListener callback) {
        this.callback = callback;
    }

    public void setDataSource(@NonNull String url) {
        setDataSource(url, null);
    }

    public void setDataSource(@NonNull String url, @Nullable Surface surface) {
        try {
            release();
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            if (progressHelper == null) {
                progressHelper = new MediaProgressHelper(this);
            }
            progressHelper.setOnPlayMediaProgressListener(this);
            System.out.println("setDataSource");
            mediaPlayer.setDataSource(url);
            if (surface != null) {
                mediaPlayer.setSurface(surface);
            }

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);

        } catch (IOException e) {
            e.printStackTrace();
            currentState = STATE_ERROR;
            if (callback != null) {
                callback.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, -1);
            }
        }
    }

    public void prepareAsync() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.prepareAsync();
                currentState = STATE_PREPARED;
            }

        } catch (Exception e) {
            e.printStackTrace();
            currentState = STATE_ERROR;
            if (callback != null) {
                callback.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, -1);
            }
        }
    }

    public void stopPlayback() {
        callback = null;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentState = STATE_IDLE;
        }

        if (progressHelper != null) {
            progressHelper.stop();
        }
    }

    public void resume() {
        if (isInPlaybackState()) {
            if (!mediaPlayer.isPlaying() && currentState == STATE_PAUSED) {
                mediaPlayer.start();
                currentState = STATE_PLAYING;

            }else if(currentState == STATE_PLAYBACK_COMPLETED){
               seekTo(0);
            }
        }

        if (progressHelper != null) {
            progressHelper.start();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                currentState = STATE_PLAYING;

            }else if(currentState == STATE_PLAYBACK_COMPLETED){
                seekTo(0);
            }
        }

        if (progressHelper != null) {
            progressHelper.start();
        }
    }

    public void pause() {
        if (isInPlaybackState()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentState = STATE_PAUSED;
            }
        }

        if (progressHelper != null) {
            progressHelper.stop();
        }
    }

    public void seekTo(int currentPosition) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(currentPosition);
        }
    }

    public void relativeSeekTo(int msec) {
        if (isInPlaybackState()) {
            seekTo(mediaPlayer.getCurrentPosition() + msec);
        }
    }

    public boolean isInPlaybackState() {
        return (mediaPlayer != null &&
                currentState != STATE_ERROR &&
                currentState != STATE_IDLE &&
                currentState != STATE_PREPARING);
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        currentState = STATE_PREPARED;
        if (callback != null) {
            callback.onPrepared(mp);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentState = STATE_PLAYBACK_COMPLETED;
        seekTo(0);
        if (callback != null) {
            callback.onCompletion(mp);
            int duration = mediaPlayer.getDuration();
            callback.onPlayMediaProgress(duration, duration);
        }

        if (progressHelper != null) {
            progressHelper.stop();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        currentState = STATE_ERROR;
        if (progressHelper != null) {
            progressHelper.stop();
        }

        if (callback != null) {
            callback.onError(mp, what, extra);
            return true;
        }
        return true;
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

    @Override
    public void onPlayMediaProgress(long duration, long currentPosition) {
        if (callback != null) {
            callback.onPlayMediaProgress(duration, currentPosition);
        }
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void setSurface(Surface surface) {
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(surface);
        }
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (callback != null) {
            callback.onVideoSizeChanged(mp, width, height);
        }
    }
}
