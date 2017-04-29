package com.example.administrator.myapplication.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (position == 1 || position == 2 || position == 3) {
//                    return 2;
//                }
//                return 1;
//            }
//        });
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerView.ItemAnimator animator = new MyDefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        animator.setMoveDuration(2000);
        animator.setChangeDuration(2000);
        recyclerView.setItemAnimator(null);
        myAdapter = new MyAdapter(this);

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

    private List<Student> getData(String desc,int count) {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Student s = new Student(desc + i, i);
            s.setId(i);
            list.add(s);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove_btn:{
                break;
            }

            case R.id.add_btn:{
                break;
            }

            case R.id.move_btn:{
                List<Student> oldList = myAdapter.getList();
                List<Student> newList = getData("newItem", 4);

                List<Student> tempNewList = new ArrayList<>();
                tempNewList.addAll(oldList);
                tempNewList.remove(7);
                tempNewList.remove(8);
                tempNewList.addAll(8, newList);

                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(oldList, tempNewList));
                myAdapter.setList(tempNewList);
                result.dispatchUpdatesTo(myAdapter);
                break;
            }

            case R.id.change_btn:{
                List<Student> oldList = myAdapter.getList();
                List<Student> newList = new ArrayList<>();
                newList.addAll(oldList);
                List<Student> list = getData("newItem", 24);
                newList.addAll(list);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(oldList, newList));
                myAdapter.setList(newList);
                result.dispatchUpdatesTo(myAdapter);
                break;
            }
        }
    }

    public static class Student{

        int id;
        String name;
        int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    private class DiffCallback extends DiffUtil.Callback{

        private List<Student> oldList;
        private List<Student> newList;

        public DiffCallback(List<Student> oldList, List<Student> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition);
        }
    }
}
