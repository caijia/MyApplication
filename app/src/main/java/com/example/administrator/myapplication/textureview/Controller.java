package com.example.administrator.myapplication.textureview;

import android.view.ViewGroup;

public interface Controller {

    void attachVideoView(VideoView view);

    void onPreparing();

    void onPrepared();

    void onStart();

    void onPause();

    void onCompletion();

    void onError();

    void onRelease();

    void onPlayProgress(long progress, long total);

    void onBufferStart(int speed);

    void onBufferEnd(int speed);

    void onBufferingUpdate(int percent);

    void setVideoUrl(String url);

    void setParentLayout(ViewGroup videoContainerParent, ViewGroup videoContainer);

    void showController();

    void hideController();

    void release();
}