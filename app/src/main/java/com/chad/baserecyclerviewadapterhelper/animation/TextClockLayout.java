package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.view.TextClock2;

public class TextClockLayout extends LinearLayout implements TextClock2.OnTextClockListener {
    public TextClockLayout(Context context) {
        super(context);
        initView(context);
    }

    public TextClockLayout(Context context,  AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextClockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TextClockLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = inflate(context, R.layout.view_text_clock, this);

    }


    @Override
    public void onTextClockChanged(CharSequence text) {

    }
}
