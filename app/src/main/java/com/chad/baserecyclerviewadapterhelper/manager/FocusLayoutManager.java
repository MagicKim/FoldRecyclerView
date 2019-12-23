package com.chad.baserecyclerviewadapterhelper.manager;

import android.animation.ValueAnimator;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;

/**
 * Created by  on 19-11-21.
 */
public class FocusLayoutManager extends RecyclerView.LayoutManager {
    /**
     * 一次完整的聚焦滑动所需要移动的距离
     */
    private float onceCompleteScrollLength = -1;
    /**
     * 水平方向累计偏移量
     */
    private long mHorizontalOffset;

    int maxLayerCount;

    private float layerPadding;

    private float normalViewGap;

    private int mFirstVisiPos;

    private int mLastVisiPos;

    private int focusdPosition = -1;

    private long autoSelectMinDuration;

    private long autoSelectMaxDuration;

    private ValueAnimator selectAnimator;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx == 0 || getItemCount() == 0) {
            return 0;
        }
        mHorizontalOffset += dx;
        dx = fill(recycler, state, dx);
        return dx;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        onceCompleteScrollLength = -1;

        detachAndScrapAttachedViews(recycler);

    }

    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int delta) {
        int resultDelta = delta;
        fillHorizontalLeft(recycler, state, delta);
        recycleChildren(recycler);
        return resultDelta;
    }

    private void recycleChildren(RecyclerView.Recycler recycler) {
    }

    //手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
    private int fillHorizontalLeft(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        if (dx < 0) {
            if (mHorizontalOffset < 0) {
                if (mFirstVisiPos - mLastVisiPos <= maxLayerCount - 1) {
                    mHorizontalOffset -= dx;
                    dx = 0;
                }
            }

            detachAndScrapAttachedViews(recycler);

            float startX = getPaddingLeft() - layerPadding;

            View tempView = null;
            int tempPosition = -1;
            if (onceCompleteScrollLength == -1) {
                tempPosition = mFirstVisiPos;
                tempView = recycler.getViewForPosition(tempPosition);
                measureChildWithMargins(tempView, 0, 0);
                onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView) + normalViewGap;

            }
            float fraction =
                    (Math.abs(mHorizontalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);

            float layerViewOffset = layerPadding * fraction;

            float normalViewOffset = onceCompleteScrollLength * fraction;

            boolean isLayerViewOffsetSetted = false;
            boolean isNormalViewOffsetSetted = false;

            //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
            mFirstVisiPos = (int) Math.floor(Math.abs(mHorizontalOffset) / onceCompleteScrollLength); //向下取整
            //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
            mLastVisiPos = getItemCount() - 1;

            int newFocusedPosition = mFirstVisiPos + maxLayerCount - 1;
            if (newFocusedPosition != focusdPosition) {
//                if (onFocusChangeListener != null) {
//                    onFocusChangeListener.onFocusChanged(newFocusedPosition, focusdPosition);
//                }
                focusdPosition = newFocusedPosition;
            }

            for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
                if (i - mFirstVisiPos < maxLayerCount) {
                    View item;
                    if (i == tempPosition && tempView != null) {
                        item = tempView;
                    } else {
                        item = recycler.getViewForPosition(i);
                    }
                    addView(item);
                    measureChildWithMargins(item, 0, 0);

                    startX += layerPadding;
                    if (!isLayerViewOffsetSetted) {
                        startX -= layerViewOffset;
                        isLayerViewOffsetSetted = true;
                    }

//                    if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
//                        for (TrasitionListener trasitionListener : trasitionListeners) {
//                            trasitionListener.handleLayerView(this, item, i - mFirstVisiPos,
//                                    maxLayerCount, i, fraction, dx);
//                        }
//                    }

                    int l, t, r, b;
                    l = (int) (startX - getDecoratedMeasurementHorizontal(item));
                    t = getPaddingTop();
                    r = (int) (startX);
                    b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                    layoutDecoratedWithMargins(item, l, t, r, b);
                } else {
                    //普通
                    View item = recycler.getViewForPosition(i);
                    addView(item);
                    measureChildWithMargins(item, 0, 0);

                    startX += onceCompleteScrollLength;
                    if (!isNormalViewOffsetSetted) {
                        startX += layerViewOffset;
                        startX -= normalViewOffset;
                        isNormalViewOffsetSetted = true;
                    }

//                    if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
//                        for (TrasitionListener trasitionListener : trasitionListeners) {
//                            if (i - mFirstVisiPos == maxLayerCount) {
//                                trasitionListener.handleFocusingView(this, item, i, fraction, dx);
//                            } else {
//                                trasitionListener.handleNormalView(this, item, i, fraction, dx);
//                            }
//                        }
//                    }

                    int l, t, r, b;
                    l = (int) startX;
                    t = getPaddingTop();
                    r = (int) (startX + getDecoratedMeasurementHorizontal(item));
                    b = getPaddingTop() + getDecoratedMeasurementVertical(item);
                    layoutDecoratedWithMargins(item, l, t, r, b);
                    //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                    if (startX + onceCompleteScrollLength > getWidth() - getPaddingRight()) {
                        mLastVisiPos = i;
                        break;
                    }
                }
            }
        }
        return dx;
    }

    private void recyclerChildren(RecyclerView.Recycler recycler) {
        List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
        for (int i = 0; i < scrapList.size(); i++) {
            RecyclerView.ViewHolder holder = scrapList.get(i);
            removeAndRecycleView(holder.itemView, recycler);
        }
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    private int findShouldSelectPosition() {
        if (onceCompleteScrollLength == -1 || mFirstVisiPos == -1) {
            return -1;
        }
        int remainder = -1;
        remainder = (int) (Math.abs(mHorizontalOffset) % onceCompleteScrollLength);

        if (remainder >= onceCompleteScrollLength / 2.0f) {
            if (mFirstVisiPos + 1 <= getItemCount() - 1) {
                return mFirstVisiPos + 1;
            }
        }
        return mFirstVisiPos;
    }

    private float getScrollToPositionOffset(int position) {
        return position * onceCompleteScrollLength - Math.abs(mHorizontalOffset);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        super.smoothScrollToPosition(recyclerView, state, position);
    }

    @Override
    public void scrollToPosition(int position) {
        mHorizontalOffset += getScrollToPositionOffset(position);
        requestLayout();
    }

    private void startValueAnimator(int position) {
        final float distance = getScrollToPositionOffset(position);
        long minDuration = autoSelectMinDuration;
        long maxDuration = autoSelectMaxDuration;
        long duration;

        float distanceFraction = (long) (Math.abs(distance) / onceCompleteScrollLength);

        if (distance <= onceCompleteScrollLength) {
            duration = (long) (minDuration + (maxDuration - minDuration) * distanceFraction);
        } else {
            duration = (long) (maxDuration * distanceFraction);
        }

        selectAnimator = ValueAnimator.ofFloat(0.0f, distance);
        selectAnimator.setDuration(duration);
        selectAnimator.setInterpolator(new LinearInterpolator());

        selectAnimator.addUpdateListener(animation -> {
            if (mHorizontalOffset < 0) {
                mHorizontalOffset =
                        (long) Math.floor(mHorizontalOffset + (float) animation.getAnimatedValue());
            } else {
                mHorizontalOffset =
                        (long) Math.ceil(mHorizontalOffset + (float) animation.getAnimatedValue());
            }
            requestLayout();
        });

        selectAnimator.start();

    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    /**
     * 滚动过程中view的变换监听接口。属于高级定制，暴露了很多关键布局数据。若定制要求不高，考虑使用{@link SimpleTrasitionListener}
     */
    public interface TransitionListener {

        /**
         * 处理在堆叠里的view。
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param viewLayer          当前层级，0表示底层，maxLayerCount-1表示顶层
         * @param maxLayerCount      最大层级
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleLayerView(FocusLayoutManager focusLayoutManager, View view, int viewLayer,
                             int maxLayerCount, int position, float fraction, float offset);

        /**
         * 处理正聚焦的那个View（即正处在从普通位置滚向聚焦位置时的那个view,即堆叠顶层view）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleFocusingView(FocusLayoutManager focusLayoutManager, View view, int position,
                                float fraction, float offset);

        /**
         * 处理不在堆叠里的普通view（正在聚焦的那个view除外）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         *                           ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        void handleNormalView(FocusLayoutManager focusLayoutManager, View view, int position,
                              float fraction, float offset);

    }

    public static abstract class SimpleTransitionListener {

        /**
         * 返回堆叠view最大透明度
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerViewMaxAlpha(int maxLayerCount) {
            return getFocusingViewMaxAlpha();
        }

        /**
         * 返回堆叠view最小透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerViewMinAlpha() {
            return 0;
        }


        /**
         * 返回堆叠view最大缩放比例
         *
         * @return
         */
        public float getLayerViewMaxScale() {
            return getFocusingViewMaxScale();
        }

        /**
         * 返回堆叠view最小缩放比例
         *
         * @return
         */
        public float getLayerViewMinScale() {
            return 0.7f;
        }


        /**
         * 返回一个百分比值，相对于"一次完整的聚焦滑动"期间，在该百分比值内view就完成缩放、透明度的渐变变化。
         * 例：若返回值为1，说明在"一次完整的聚焦滑动"期间view将线性均匀完成缩放、透明度变化；
         * 例：若返回值为0.5，说明在"一次完整的聚焦滑动"的一半路程内（具体从什么时候开始变由实际逻辑自己决定），view将完成的缩放、透明度变化
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getLayerChangeRangePercent() {
            return 0.35f;
        }

        /**
         * 返回聚焦view的最大透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewMaxAlpha() {
            return 1f;
        }

        /**
         * 返回聚焦view的最小透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewMinAlpha() {
            return getNormalViewAlpha();
        }

        /**
         * 返回聚焦view的最大缩放比例
         *
         * @return
         */
        public float getFocusingViewMaxScale() {
            return 1.2f;
        }

        /**
         * 返回聚焦view的最小缩放比例
         *
         * @return
         */
        public float getFocusingViewMinScale() {
            return getNormalViewScale();
        }

        /**
         * 返回值意义参考{@link #getLayerChangeRangePercent()}
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getFocusingViewChangeRangePercent() {
            return 0.5f;
        }

        /**
         * 返回普通view的透明度
         *
         * @return
         */
        @FloatRange(from = 0.0f, to = 1.0f)
        public float getNormalViewAlpha() {
            return 1.0f;
        }

        /**
         * 返回普通view的缩放比例
         *
         * @return
         */
        public float getNormalViewScale() {
            return 1.0f;
        }

    }

    public static class TransitionListenerConvert implements TransitionListener {

        SimpleTransitionListener stl;

        public TransitionListenerConvert(SimpleTransitionListener simpleTransitionListener) {
            stl = simpleTransitionListener;
        }

        @Override
        public void handleLayerView(FocusLayoutManager focusLayoutManager, View view, int viewLayer,
                                    int maxLayerCount, int position, float fraction, float offset) {
            float realFraction;
            if (fraction <= stl.getLayerChangeRangePercent()) {
                realFraction = fraction / stl.getLayerChangeRangePercent();
            } else {
                realFraction = 1.0f;
            }
            float minScale = stl.getLayerViewMinScale();
            float maxScale = stl.getLayerViewMaxScale();
            float scaleDelta = maxScale - minScale;

            float currentLayerMaxScale = minScale + scaleDelta * (viewLayer + 1) / (maxLayerCount * 1.0f);

            float currentLayerMinScale = minScale + scaleDelta * viewLayer / (maxLayerCount * 1.0f);

            float realScale = currentLayerMaxScale - (currentLayerMaxScale - currentLayerMinScale) * realFraction;

            float minAlpha = stl.getLayerViewMinAlpha();
            float maxAlpha = stl.getLayerViewMaxAlpha(maxLayerCount);
            float alphaDelta = maxAlpha - minAlpha; //总透明度差
            float currentLayerMaxAlpha =
                    minAlpha + alphaDelta * (viewLayer + 1) / (maxLayerCount * 1.0f);
            float currentLayerMinAlpha = minAlpha + alphaDelta * viewLayer / (maxLayerCount * 1.0f);

            float realAlpha =
                    currentLayerMaxAlpha - (currentLayerMaxAlpha - currentLayerMinAlpha) * realFraction;

            view.setScaleX(realScale);
            view.setScaleY(realScale);
            view.setAlpha(realAlpha);
        }

        @Override
        public void handleFocusingView(FocusLayoutManager focusLayoutManager, View view,
                                       int position, float fraction, float offset) {
            float realFraction;
            if (fraction <= stl.getFocusingViewChangeRangePercent()) {
                realFraction = fraction / stl.getFocusingViewChangeRangePercent();
            } else {
                realFraction = 1.0f;
            }

            float realScale =
                    stl.getFocusingViewMinScale() + (stl.getFocusingViewMaxScale() - stl.getFocusingViewMinScale()) * realFraction;
            float realAlpha =
                    stl.getFocusingViewMinAlpha() + (stl.getFocusingViewMaxAlpha() - stl.getFocusingViewMinAlpha()) * realFraction;

            view.setScaleX(realScale);
            view.setScaleY(realScale);
            view.setAlpha(realAlpha);

        }

        @Override
        public void handleNormalView(FocusLayoutManager focusLayoutManager, View view, int position,
                                     float fraction, float offset) {
            view.setScaleX(stl.getNormalViewScale());
            view.setScaleY(stl.getNormalViewScale());
            view.setAlpha(stl.getNormalViewAlpha());
        }
    }


}
