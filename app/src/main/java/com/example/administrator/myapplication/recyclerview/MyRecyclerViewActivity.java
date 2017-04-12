package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.recyclerview.adapter.MyAdapter;
import com.example.administrator.myapplication.recyclerview.itemanimator.MyDefaultItemAnimator;
import com.example.administrator.myapplication.recyclerview.itemdecoration.GridSpacingItemDecoration;
import com.example.administrator.myapplication.recyclerview.itemtouchhelper.MyItemTouchHelper;
import com.example.administrator.myapplication.recyclerview.itemtouchhelper.MyItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/4/7 0007
 */

public class MyRecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recyclerview);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        int color = ResourcesCompat.getColor(getResources(), R.color.divider, getTheme());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(12,true,color));
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        RecyclerView.ItemAnimator animator = new MyDefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        animator.setMoveDuration(2000);
        animator.setChangeDuration(2000);
        recyclerView.setItemAnimator(animator);
        myAdapter = new MyAdapter(this, getData());

        MyItemTouchHelperCallback helperCallback = new MyItemTouchHelperCallback();
        MyItemTouchHelper mIth = new MyItemTouchHelper(helperCallback);
        helperCallback.setOnItemActionListener(new MyItemTouchHelperCallback.OnItemActionListener() {
            @Override
            public void onMove(int fromPos, int toPos) {
                myAdapter.swapPosition(fromPos, toPos);
            }

            @Override
            public void onSwiped(int removePos) {
                myAdapter.remove(removePos);
            }
        });

        mIth.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(myAdapter);


        final Button removeBtn = (Button) findViewById(R.id.remove_btn);
        final Button addBtn = (Button) findViewById(R.id.add_btn);
        final Button moveBtn = (Button) findViewById(R.id.move_btn);
        final Button changeBtn = (Button) findViewById(R.id.change_btn);
        removeBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        moveBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);

    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            list.add("item " + (i + 1));
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_btn:{
                myAdapter.remove(1);
                break;
            }

            case R.id.add_btn:{
                myAdapter.add(1);
                break;
            }

            case R.id.move_btn:{
                myAdapter.swapPosition(0,1);
                break;
            }

            case R.id.change_btn:{
                myAdapter.change(1);
                break;
            }
        }
    }
}
