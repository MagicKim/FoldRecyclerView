package com.chad.baserecyclerviewadapterhelper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;


/**
 * 显示空布局
 */
public class EmptyRecyclerView extends RecyclerView {

    private static final String TAG = "EmptyRecyclerView";
    private LinearLayout mEmptyView;
    private Handler mHandler = new Handler();

    public EmptyRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        int mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Log.w(TAG, "onChanged");
//            setEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            Log.w(TAG, "onItemRangeRemoved, itemCount = " + itemCount);
//            setEmptyView();
        }
    };

    private void setEmptyView() {
        Adapter<?> adapter = getAdapter();
        if (adapter != null && mEmptyView != null) {
            Log.d(TAG, "----emptyObserver----");
            if (adapter.getItemCount() == 1) {
                mHandler.post(showEmptyLayout);
            } else {
                mHandler.post(showRecyclerViewLayout);
            }
        }
    }


    private Runnable showEmptyLayout = new Runnable() {
        @Override
        public void run() {
            mEmptyView.setVisibility(View.VISIBLE);
            EmptyRecyclerView.this.setVisibility(View.GONE);
        }
    };

    private Runnable showRecyclerViewLayout = new Runnable() {
        @Override
        public void run() {
            mEmptyView.setVisibility(View.GONE);
            EmptyRecyclerView.this.setVisibility(View.VISIBLE);
        }
    };

    /**
     * * @param emptyView 展示的空view
     */
    public void setEmptyView(LinearLayout emptyView) {
        mEmptyView = emptyView;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            Log.e(TAG,"setAdapter");
            adapter.registerAdapterDataObserver(emptyObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
