package com.example.administrator.myapplication.pull;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/2/15 0015
 */

public class TestListAdapter extends ArrayAdapter<String> {

    public TestListAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public int getCount() {
        return 15;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return position+"";
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_test_pull, parent, false);

             textView = (TextView) convertView.findViewById(R.id.test_view);
            convertView.setTag(textView);
        } else {
             textView = (TextView) convertView.getTag();
        }
        textView.setText(getItem(position)+"ITEM");
        return convertView;
    }
}
