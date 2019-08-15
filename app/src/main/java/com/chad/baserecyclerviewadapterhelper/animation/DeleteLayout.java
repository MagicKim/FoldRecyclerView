package com.chad.baserecyclerviewadapterhelper.animation;

import android.animation.Animator;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;


public class DeleteLayout extends RelativeLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private ImageView imageView;
    private Button button;
    private static DeleteLayout deleteLayoutCache;
    private int mMorphCounter = 0;
    private Handler mHandler = new Handler();

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
        button = findViewById(R.id.bt_del);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
        imageView.setVisibility(VISIBLE);
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
                restoreUI();
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
            setSquareIcon();
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(1);
            }
        } else if (mMorphCounter == 2) {
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(2);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    restoreUI();
                }
            }, 500);
        }
    }

    public void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        mMorphCounter = 0;
        setCircleIcon();
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

    public void setSquareIcon() {
        imageView.setVisibility(GONE);
        button.setVisibility(VISIBLE);
    }

    public void setCircleIcon() {
        button.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }

    public CountDownTimer getCountDownTimer() {
        return timer;
    }


}
