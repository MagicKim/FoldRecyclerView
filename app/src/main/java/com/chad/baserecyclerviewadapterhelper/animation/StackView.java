package com.chad.baserecyclerviewadapterhelper.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by ${Kim} on 19-5-9.
 */
public class StackView extends FrameLayout {
    private static final String TAG = "StackView";
    private Context mContext;
    private CardConfig CardConfig;

    public StackView(Context context) {
        this(context, null);
    }

    public StackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        CardConfig = new CardConfig();
    }

    public class CardConfig {
        public int MAX_VISIBLE_COUNT = 3;
        public int BASE_TRANSLATION_Y = dip2px(mContext, 20);
        public float BASE_SCALE = 0.08F;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新定义父View的高度，否则会导致子View被遮挡;
        float parentHeight;
        float scaleHeight = 0;
        int childHeight = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            if (childView != null) {
                scaleHeight += childView.getTranslationY() - (childView.getTranslationY() * childView.getScaleY());
                if (i == getChildCount() - 1) {//只需测量第一个高度即可
                    childHeight = childView.getHeight();
                }
            }
        }
        parentHeight = childHeight + scaleHeight;
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) parentHeight);
        Log.d(TAG, "onMeasure: " + parentHeight + " (int) parentHeight)==" + (int) parentHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getChildCount() == 0) {
            initView();
        }
    }

    //初始化布局
    public void initView() {
        int layoutCount = CardConfig.MAX_VISIBLE_COUNT;
        int viewLevel;//View的层级
        for (int pos = 0; pos <= layoutCount; pos++) {
            View childView = getChildAt(pos);
            viewLevel = getChildCount();
            if (pos == layoutCount) {
                viewLevel = pos - 1;
            }
            childView.setTranslationY(viewLevel * CardConfig.BASE_TRANSLATION_Y);
            childView.setScaleX(1 - viewLevel * CardConfig.BASE_SCALE);
            childView.setScaleY(1 - viewLevel * CardConfig.BASE_SCALE);
            addViewInLayout(childView, 0, childView.getLayoutParams());
            childView.requestLayout();
        }
        Log.d(TAG, "onLayout:" + getChildCount());
    }


    @Override
    protected void onDetachedFromWindow() {//view从屏幕移除时候及时销毁动画
        super.onDetachedFromWindow();

    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(mContext, attrs);
    }

    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */

    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);

    }
}
