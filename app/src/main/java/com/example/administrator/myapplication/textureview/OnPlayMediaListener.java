package com.example.administrator.myapplication.textureview;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface OnPlayMediaListener {

    void onBufferingUpdate(IMediaPlayer mp, int percent);

    void onCompletion(IMediaPlayer mp);

    boolean onError(IMediaPlayer mp, int what, int extra);

    boolean onInfo(IMediaPlayer mp, int what, int extra);

    void onPrepared(IMediaPlayer mp);

    void onVideoSizeChanged(IMediaPlayer mp, int width, int height);

    void onPlayMediaProgress(long duration, long currentPosition);

    void onStart();

    void onPause();

    void onPreparing();
}