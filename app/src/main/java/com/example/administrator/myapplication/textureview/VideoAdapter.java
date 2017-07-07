package com.example.administrator.myapplication.textureview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

import java.util.List;

/**
 * Created by cai.jia on 2017/7/7 0007
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {

    private List<String> list;

    public VideoAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public VideoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoVH(view);
    }

    @Override
    public void onBindViewHolder(VideoVH holder, int position) {
        holder.playVideoView.setVideoUrl(list.get(position));
        if (position == 0) {
            holder.playVideoView.resumeInRecyclerView();
        }else{
            holder.playVideoView.pauseInRecyclerView();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VideoVH extends RecyclerView.ViewHolder{
        PlayVideoView playVideoView;
        public VideoVH(View itemView) {
            super(itemView);
            playVideoView = (PlayVideoView) itemView.findViewById(R.id.play_video_view);
        }
    }
}
