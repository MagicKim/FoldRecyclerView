package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;


public class HeaderDelButton extends RelativeLayout implements View.OnClickListener {
    private OnDeleteItemListener onDeleteItemListener;
    private CountDownTimer timer;
    private MorphingButton morphingButton;
    private static HeaderDelButton deleteLayoutCache;
    private boolean isHeaderDel;
    private int mMorphCounter;

    public HeaderDelButton(@NonNull Context context) {
        this(context, null);
    }

    public HeaderDelButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderDelButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DeleteLayout);
            isHeaderDel = a.getBoolean(R.styleable.DeleteLayout_dl_header, true);
        }
        initView(context);
        initTimer();
    }

    private void initView(Context context) {
        inflate(context, R.layout.item_header_delete_layout, this);
        morphingButton = findViewById(R.id.mb_icon);
        morphingButton.setOnClickListener(this);
        if (isHeaderDel) {
            morphToCircle(morphingButton, 0);
        } else {
            morphParentToCircle(morphingButton, 0);
        }

    }

    public static HeaderDelButton getViewCache() {
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
            deleteLayoutCache = HeaderDelButton.this;
            if (isHeaderDel) {
                morphToSquare(morphingButton, 250);
            } else {
                morphParentToSquare(morphingButton, 250);
            }
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(1);
            }
        } else if (mMorphCounter == 2) {
            if (onDeleteItemListener != null) {
                onDeleteItemListener.setDeleteItem(2);
            }
            restoreUI();
            deleteLayoutCache = null;
        }
    }

    public void restoreUI() {
        if (timer != null) {
            timer.cancel();
        }
        if (isHeaderDel) {
            morphToCircle(morphingButton, 0);
        } else {
            morphParentToCircle(morphingButton, 0);
        }
        mMorphCounter = 0;
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .strokeColor(R.color.white_text_color)
                .color(R.color.colorAccent)
                .cornerRadius(dimen(R.dimen.list_view_del_button_width))
                .width(dimen(R.dimen.list_view_del_button_width))
                .height(dimen(R.dimen.list_view_del_button_height))
                .textColor(color(R.color.notify_recycler_view_item_header_normal_color))
                .textSize(dimen(R.dimen.list_view_item_header_del))
                .text("清空");
        btnMorph.morph(square);
    }

    private void morphToCircle(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.list_view_del_oval_size))
                .width(dimen(R.dimen.list_view_del_oval_size))
                .height(dimen(R.dimen.list_view_del_oval_size))
                .color(color(R.color.notify_recycler_content_bg_normal_color))
                .icon(R.drawable.item_icon_delete_all);
        btnMorph.morph(circle);
    }

    private void morphParentToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .width(dimen(R.dimen.list_view_parent_collapse_width))
                .cornerRadius(dimen(R.dimen.list_view_del_button_radius))
                .color(color(R.color.notify_recycler_content_bg_normal_color))
                .height(dimen(R.dimen.list_view_parent_collapse_height))
                .textColor(color(R.color.notify_recycler_view_item_header_normal_color))
                .textSize(dimen(R.dimen.list_view_parent_del))
                .text("清空");
        btnMorph.morph(square);
    }

    private void morphParentToCircle(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.list_view_parent_delete_size))
                .width(dimen(R.dimen.list_view_parent_delete_size))
                .height(dimen(R.dimen.list_view_parent_delete_size))
                .color(color(R.color.notify_recycler_content_bg_normal_color))
                .icon(R.drawable.notify_parent_del);
        btnMorph.morph(circle);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public String string(@StringRes int stringId) {
        return getResources().getString(stringId);
    }


    public void setDeleteItemListener(OnDeleteItemListener deleteItemListener) {
        onDeleteItemListener = deleteItemListener;
    }

    public interface OnDeleteItemListener {
        void setDeleteItem(int state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        restoreUI();
        if (this == deleteLayoutCache) {
            deleteLayoutCache = null;
        }
    }

}
