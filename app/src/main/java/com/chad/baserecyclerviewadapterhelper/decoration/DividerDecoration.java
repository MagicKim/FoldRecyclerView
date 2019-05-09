package com.chad.baserecyclerviewadapterhelper.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;


/**
 * 设置recycler view的分割线
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int mOrientation;
    private int mItemMarginStart;

    public DividerDecoration(Context context, int orientation) {
        mItemMarginStart = (int) context.getResources().getDimension(R.dimen.list_view_item_offset);
        this.setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        } else {
            this.mOrientation = orientation;
        }
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int layoutPosition = parent.getChildLayoutPosition(view);
        if (this.mOrientation == 1) {
            if (layoutPosition == 0) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mItemMarginStart);
            }

        } else {
            outRect.set(0, 0, mItemMarginStart, 0);
        }
    }
}

