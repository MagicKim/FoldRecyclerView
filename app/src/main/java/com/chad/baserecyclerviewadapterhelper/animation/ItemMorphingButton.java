package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;


public class ItemMorphingButton extends RelativeLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private MorphingButton imageView;
    private int mMorphCounter2 = 1;

    public ItemMorphingButton(@NonNull Context context) {
        super(context);
        initView(context);
        initTimer();
    }

    public ItemMorphingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initTimer();
    }

    public ItemMorphingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTimer();
    }

    private void initView(Context context) {
        inflate(context, R.layout.item_parent_delete_layout, this);
        imageView = findViewById(R.id.iv_icon);
        imageView.setOnClickListener(this);
        morphToFailure(imageView, 0);
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
        onMorphButton2Clicked();
    }

    public void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        mMorphCounter2 = 1;
        morphToFailure(imageView, integer(R.integer.mb_animation));
    }

    private void onMorphButton2Clicked() {
        if (mMorphCounter2 == 0) {
            mMorphCounter2++;
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem();
            }
            restoreUI();
        } else if (mMorphCounter2 == 1) {
            timer.start();
            mMorphCounter2 = 0;
            morphToSquare(imageView, integer(R.integer.mb_animation));
        }
    }

    public void setDeleteItemListener(OnDeleteItemListener deleteItemListener) {
        onDeleteItemListener = deleteItemListener;
    }

    public interface OnDeleteItemListener {
        void setDeleteItem();
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.list_view_del_button_radius))
                .width(dimen(R.dimen.list_view_del_button_width))
                .height(dimen(R.dimen.list_view_del_button_height))
                .color(color(R.color.notify_recycler_content_bg_normal_color))
                .colorPressed(color(R.color.notify_recycler_content_bg_normal_color))
                .textSize(dimen(R.dimen.list_view_item_header_del))
                .textColor(color(R.color.notify_recycler_view_item_header_normal_color))
                .text("清除全部");
        btnMorph.morph(square);
    }

    private void morphToFailure(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.list_view_del_oval_size))
                .width(dimen(R.dimen.list_view_del_oval_size))
                .height(dimen(R.dimen.list_view_del_oval_size))
                .color(color(R.color.notify_recycler_content_bg_normal_color))
                .icon(R.drawable.item_icon_delete_all);
        btnMorph.morph(circle);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

}
