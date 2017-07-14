package com.example.administrator.myapplication.textureview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends Activity  {
    private String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
//    private String url = "http://jetsunfile.cn-gd.ufileos.com/upload_29048693_868026024793198_1498553210";

    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter adapter = new VideoAdapter(getData());
        recyclerView.setAdapter(adapter);
    }

    public List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(url + (i == 0 ? "" : +i));
        }
        return list;
    }
}