package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.myapplication.R;

public class PlayVideoActivity extends Activity {
//    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
    private String url = "http://jetsunfile.cn-gd.ufileos.com/upload_29048693_868026024793198_1498553210";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        PlayVideoView playVideoView = (PlayVideoView) findViewById(R.id.play_video_view);
        playVideoView.setVideoUrl(url);
    }
}