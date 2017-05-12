package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2016/6/7 0007.
 */
public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private TextView mErrorView;

    private TextView mTheEndView;

    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_load_more_footer, this, true);

        mLoadingView = findViewById(R.id.loadingView);
        TextView loadingTextTv = (TextView) mLoadingView.findViewById(R.id.text);
        mErrorView = (TextView) findViewById(R.id.errorView);
        mTheEndView = (TextView) findViewById(R.id.theEndView);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreFooterView);
            String loadingText = a.getString(R.styleable.LoadMoreFooterView_loading_text);
            String errorText = a.getString(R.styleable.LoadMoreFooterView_error_text);
            String endText = a.getString(R.styleable.LoadMoreFooterView_end_text);
            int color = a.getResourceId(R.styleable.LoadMoreFooterView_lf_color,-1);

            if (!TextUtils.isEmpty(loadingText)) {
                loadingTextTv.setText(loadingText);
            }

            if (!TextUtils.isEmpty(errorText)) {
                mErrorView.setText(loadingText);
            }

            if (!TextUtils.isEmpty(endText)) {
                mTheEndView.setText(loadingText);
            }

            if (color != -1) {
                int newColor = getResources().getColor(color);
                loadingTextTv.setTextColor(newColor);
                mErrorView.setTextColor(newColor);
                mTheEndView.setTextColor(newColor);
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(Status.GONE);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        if (status == null) {
            return;
        }
        this.mStatus = status;
        change();
    }

    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
    }

    private void change() {
        switch (mStatus) {
            case GONE:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                break;

            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }

    public enum Status {
        GONE, LOADING, ERROR, THE_END
    }

    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }
}
