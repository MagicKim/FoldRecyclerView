package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;


public class ItemDeleteLayout extends RelativeLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private ImageView imageView;
    private Button button;
    private RelativeLayout relativeLayout;

    public ItemDeleteLayout(@NonNull Context context) {
        super(context);
        initView(context);
        initTimer();
    }

    public ItemDeleteLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initTimer();
    }

    public ItemDeleteLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTimer();
    }

    private void initView(Context context) {
        inflate(context, R.layout.item_parent_delete_layout, this);
        relativeLayout = findViewById(R.id.rl_delete);

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

    public void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        button.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }

    public void setDeleteItemListener(OnDeleteItemListener deleteItemListener) {
        onDeleteItemListener = deleteItemListener;
    }

    public interface OnDeleteItemListener {
        void setDeleteItem();
    }

}
