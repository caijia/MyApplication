package com.example.administrator.myapplication.behavior;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/2/22 0022
 */

public class BehaviorActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);

        TextView swipeView = (TextView) findViewById(R.id.swipe_text);
        final SwipeDismissBehavior<View> swipe = new SwipeDismissBehavior<>();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
        swipe.setListener(
                new SwipeDismissBehavior.OnDismissListener() {
                    @Override public void onDismiss(View view) {
                    }

                    @Override
                    public void onDragStateChanged(int state) {}
                });

        CoordinatorLayout.LayoutParams coordinatorParams =
                (CoordinatorLayout.LayoutParams) swipeView.getLayoutParams();
        coordinatorParams.setBehavior(swipe);
    }
}
