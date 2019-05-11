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

import com.chad.baserecyclerviewadapterhelper.R;


public class DeleteLayout extends FrameLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private ImageView imageView;
    private Button button;
    private boolean isDeleteAllShow = false;

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
        switch (v.getId()) {
            case R.id.iv_icon:
                isDeleteAllShow = true;
                imageView.setVisibility(GONE);
                button.setVisibility(VISIBLE);
                timer.start();
                break;
            case R.id.bt_del:
                if (onDeleteItemListener != null) {
                    onDeleteItemListener.setDeleteItem();
                }
                restoreUI();
                break;
        }
    }

    private void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        isDeleteAllShow = false;
        button.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }

    public void setRestoreUI(boolean isRestore) {
        if (isRestore) {
            restoreUI();
        }
    }

    public void setDeleteItemListener(OnDeleteItemListener deleteItemListener) {
        onDeleteItemListener = deleteItemListener;
    }

    public interface OnDeleteItemListener {
        void setDeleteItem();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDeleteAllShow = false;
        restoreUI();

    }


}
