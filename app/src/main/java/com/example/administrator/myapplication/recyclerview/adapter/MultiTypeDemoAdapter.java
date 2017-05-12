package com.example.administrator.myapplication.recyclerview.adapter;

import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.TextViewDelegate;
import com.example.administrator.myapplication.recyclerview.multiType.AbsDelegationAdapter;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class MultiTypeDemoAdapter extends AbsDelegationAdapter<Object> {

    public MultiTypeDemoAdapter() {
        delegateManager.addDelegate(new TextViewDelegate());
    }
}
