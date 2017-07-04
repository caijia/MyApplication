package com.example.administrator.myapplication.textureview;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by cai.jia on 2017/7/4 0004
 */

public class VideoControllerHelper implements OnPlayMediaListener {

    private Controller controller;

    public VideoControllerHelper(Controller controller) {
        this.controller = controller;
    }

    public void attachVideoView(VideoView videoView) {
        videoView.setOnPlayMediaListener(this);
        if (controller != null) {
            controller.attachVideoView(videoView);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        if (controller != null) {
            controller.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        if (controller != null) {
            controller.onCompletion();
        }
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (controller != null) {
            controller.onError();
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START: {
                if (controller != null) {
                    controller.onBufferStart(extra);
                }
                break;
            }

            case IMediaPlayer.MEDIA_INFO_BUFFERING_END: {
                if (controller != null) {
                    controller.onBufferEnd(extra);
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        if (controller != null) {
            controller.onPrepared();
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height) {
    }

    @Override
    public void onPlayMediaProgress(long duration, long currentPosition) {
        if (controller != null) {
            controller.onPlayProgress(currentPosition, duration);
        }
    }

    @Override
    public void onStart() {
        if (controller != null) {
            controller.onStart();
        }
    }

    @Override
    public void onPause() {
        if (controller != null) {
            controller.onPause();
        }
    }

    @Override
    public void onPreparing() {
        if (controller != null) {
            controller.onPreparing();
        }
    }
}
