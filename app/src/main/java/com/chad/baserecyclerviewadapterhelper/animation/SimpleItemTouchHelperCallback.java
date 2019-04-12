package com.chad.baserecyclerviewadapterhelper.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.baserecyclerviewadapterhelper.R;
import com.chad.baserecyclerviewadapterhelper.adapter.ExpandableItemAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by ${Kim} on 19-3-18.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ExpandableItemAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ExpandableItemAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getAdapterPosition() == 0) {
            return 0;
        }
        if (viewHolder.getItemViewType() == ExpandableItemAdapter.TYPE_NORMAL) {
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(0, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
//        if (viewHolder != null) {
//            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
//            // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//            CardView cardView = baseViewHolder.getView(R.id.card_view_normal);
//            getDefaultUIUtil().onSelected(cardView);
//        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 释放View时回调，清除背景颜色，隐藏图标
        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
//        ImageView imageView = baseViewHolder.getView(R.id.iv_swipe_del);
//        CardView cardView = baseViewHolder.getView(R.id.card_view_normal);
//        RelativeLayout relativeLayout = baseViewHolder.getView(R.id.rl_normal_root_layout);
//        getDefaultUIUtil().clearView(cardView);
//        imageView.setVisibility(View.GONE);
//        relativeLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // ItemTouchHelper的onDraw方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的下方
        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
//        ImageView imageView = baseViewHolder.getView(R.id.iv_swipe_del);
//        RelativeLayout relativeLayout = baseViewHolder.getView(R.id.rl_normal_root_layout);
//        CardView cardView = baseViewHolder.getView(R.id.card_view_normal);
//        getDefaultUIUtil().onDraw(c, recyclerView, cardView, dX, dY, actionState, isCurrentlyActive);
//        if (dX < 0) { // 向左滑动是的提示
//            relativeLayout.setBackgroundResource(R.color.color_light_blue);
//            imageView.setVisibility(View.VISIBLE);
//        }
//        if (dX < 0) { // 向右滑动时的提示
//            ((SampleAdapter.ItemViewHolder) viewHolder).vBackground.setBackgroundResource(R.color.colorSchedule);
//            ((SampleAdapter.ItemViewHolder) viewHolder).ivSchedule.setVisibility(View.VISIBLE);
//            ((SampleAdapter.ItemViewHolder) viewHolder).ivDone.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // ItemTouchHelper的onDrawOver方法会调用该方法，可以使用Canvas对象进行绘制，绘制的图案会在RecyclerView的上方
        // 默认是操作ViewHolder的itemView，这里调用ItemTouchUIUtil的clearView方法传入指定的view
//        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
//        CardView cardView = baseViewHolder.getView(R.id.card_view_normal);
//        getDefaultUIUtil().onDrawOver(c, recyclerView, cardView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }
}
