package com.example.administrator.myapplication.textureview;

import android.media.MediaPlayer;

public interface OnPlayMediaListener {

    void onBufferingUpdate(MediaPlayer mp, int percent);

    void onCompletion(MediaPlayer mp);

    boolean onError(MediaPlayer mp, int what, int extra);

    boolean onInfo(MediaPlayer mp, int what, int extra);

    void onPrepared(MediaPlayer mp);

    void onVideoSizeChanged(MediaPlayer mp, int width, int height);

    void onPlayMediaProgress(long duration, long currentPosition);
}