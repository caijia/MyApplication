package com.example.administrator.myapplication.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * Created by aspsine on 16/3/14.
 */
public class ClassicRefreshHeaderView extends RelativeLayout implements RefreshTrigger {
    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;

    private ProgressBar progressBar;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public ClassicRefreshHeaderView(Context context) {
        this(context, null);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_irecyclerview_classic_refresh_header_view, this);

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
        ivArrow = (ImageView) findViewById(R.id.ivArrow);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    public void onStart(float headerViewHeight) {
        progressBar.setIndeterminate(false);
    }

    @Override
    public void onMove(float moved, float headerViewHeight) {
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        ivSuccess.setVisibility(GONE);
        if (moved <= headerViewHeight) {
            if (rotated) {
                ivArrow.clearAnimation();
                ivArrow.startAnimation(rotateDown);
                rotated = false;
            }

            tvRefresh.setText(getString(R.string.pull_to_refresh));
        } else {
            tvRefresh.setText(getString(R.string.release_to_refresh));
            if (!rotated) {
                ivArrow.clearAnimation();
                ivArrow.startAnimation(rotateUp);
                rotated = true;
            }
        }
    }

    @Override
    public void onRefreshing() {
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText(getString(R.string.refreshing));
    }

    private String getString(@StringRes int stringResId) {
        return getResources().getString(stringResId);
    }

    @Override
    public void onRefreshComplete() {
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText(getString(R.string.complete));
    }

    @Override
    public void onReset() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    @Override
    public int refreshDistance(int headerViewHeight) {
        return headerViewHeight;
    }

    @Override
    public int dragRange(int headerViewHeight) {
        return headerViewHeight * 2;
    }
}
