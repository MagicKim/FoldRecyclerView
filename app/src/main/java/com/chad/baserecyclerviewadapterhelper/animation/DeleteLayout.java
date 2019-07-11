package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;


public class DeleteLayout extends FrameLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private ImageView imageView;
    private Button button;
    private static DeleteLayout deleteLayoutCache;
    private int mMorphCounter;

    public DeleteLayout(@NonNull Context context) {
        super(context);
        initView(context);
        initTimer();
    }

    public DeleteLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initTimer();
    }

    public DeleteLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTimer();
    }

    private void initView(Context context) {
        inflate(context, R.layout.delete_custom_layout, this);
        imageView = findViewById(R.id.iv_icon);
        imageView.setOnClickListener(this);
        button = findViewById(R.id.bt_del);
        button.setOnClickListener(this);
        button.setVisibility(GONE);
    }

    public static DeleteLayout getViewCache() {
        return deleteLayoutCache;
    }

    private void initTimer() {
        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                button.setVisibility(GONE);
                imageView.setVisibility(VISIBLE);
            }
        };
    }

    @Override
    public void onClick(View v) {
        mMorphCounter++;
        if (mMorphCounter == 1) {
            if (deleteLayoutCache != null) {
                if (deleteLayoutCache != this) {
                    deleteLayoutCache.restoreUI();
                }
            }
            timer.start();
            deleteLayoutCache = DeleteLayout.this;
            imageView.setVisibility(GONE);
            button.setVisibility(VISIBLE);
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(1);
            }
        } else if (mMorphCounter == 2) {
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(2);
            }
            if (timer != null) {
                timer.cancel();
            }
            mMorphCounter = 0;
            deleteLayoutCache = null;
        }
    }

    private void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        mMorphCounter = 0;
        button.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }


    public void setDeleteItemListener(OnDeleteItemListener deleteItemListener) {
        onDeleteItemListener = deleteItemListener;
    }

    public interface OnDeleteItemListener {
        void setDeleteItem(int status);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


}
