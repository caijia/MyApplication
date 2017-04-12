package com.example.administrator.myapplication.recyclerview.itemtouchhelper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by cai.jia on 2017/4/7 0007
 */

public class MyItemTouchHelperCallback extends MyItemTouchHelper.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = MyItemTouchHelper.UP | MyItemTouchHelper.DOWN | MyItemTouchHelper.START |
                MyItemTouchHelper.END;
        int swipeFlag = MyItemTouchHelper.START | MyItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();
        if (onItemActionListener != null) {
            onItemActionListener.onMove(fromPos, toPos);
        }
        return true; //true if moved, false otherwise
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int removePos = viewHolder.getAdapterPosition();
        if (onItemActionListener != null) {
            onItemActionListener.onSwiped(removePos);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public interface OnItemActionListener{

        void onMove(int fromPos, int toPos);

        void onSwiped(int removePos);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.onItemActionListener = onItemActionListener;
    }

    private OnItemActionListener onItemActionListener;
}
