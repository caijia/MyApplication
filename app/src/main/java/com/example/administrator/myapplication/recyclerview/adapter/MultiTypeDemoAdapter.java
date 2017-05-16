package com.example.administrator.myapplication.recyclerview.adapter;

import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.LoadMoreDelegate;
import com.example.administrator.myapplication.recyclerview.adapter.itemViewDelegate.TextViewDelegate;
import com.example.administrator.myapplication.recyclerview.multiType.LoadMoreDelegationAdapter;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class MultiTypeDemoAdapter extends LoadMoreDelegationAdapter {

    public MultiTypeDemoAdapter(boolean loadMore, LoadMoreDelegate.OnLoadMoreDelegateListener listener) {
        super(loadMore, listener);
        delegateManager.addDelegate(new TextViewDelegate());
    }
}
