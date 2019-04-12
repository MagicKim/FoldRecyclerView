package com.chad.baserecyclerviewadapterhelper.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.baserecyclerviewadapterhelper.R;

import static com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout.State.COLLAPSED;
import static com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout.State.COLLAPSING;
import static com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout.State.EXPANDED;
import static com.chad.baserecyclerviewadapterhelper.animation.FoldingLayout.State.EXPANDING;


public class FoldingLayout extends FrameLayout {
    public interface State {
        int COLLAPSED = 0;
        int COLLAPSING = 1;
        int EXPANDING = 2;
        int EXPANDED = 3;
    }

    public static final String KEY_SUPER_STATE = "super_state";
    public static final String KEY_EXPANSION = "expansion";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final int DEFAULT_DURATION = 300;

    private int duration = DEFAULT_DURATION;
    private float parallax;
    private float expansion;
    private int orientation;
    private int state;

    private Interpolator interpolator = new LinearInterpolator();
    private ValueAnimator animator;

    private OnExpansionUpdateListener listener;

    public FoldingLayout(Context context) {
        this(context, null);
    }

    public FoldingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FoldingLayout);
            duration = a.getInt(R.styleable.FoldingLayout_el_duration, DEFAULT_DURATION);
            expansion = a.getBoolean(R.styleable.FoldingLayout_el_expanded, false) ? 1 : 0;
            orientation = a.getInt(R.styleable.FoldingLayout_android_orientation, VERTICAL);
            parallax = a.getFloat(R.styleable.FoldingLayout_el_parallax, 1);
            a.recycle();

            state = expansion == 0 ? COLLAPSED : EXPANDED;
            setParallax(parallax);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();

        expansion = isExpanded() ? 1 : 0;

        bundle.putFloat(KEY_EXPANSION, expansion);
        bundle.putParcelable(KEY_SUPER_STATE, superState);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        expansion = bundle.getFloat(KEY_EXPANSION);
        state = expansion == 1 ? EXPANDED : COLLAPSED;
        Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);

        super.onRestoreInstanceState(superState);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int size = orientation == LinearLayout.HORIZONTAL ? width : height;

        setVisibility(expansion == 0 && size == 0 ? GONE : VISIBLE);

        int expansionDelta = size - Math.round(size * expansion);
        if (parallax > 0) {
            float parallaxDelta = expansionDelta * parallax;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (orientation == HORIZONTAL) {
                    int direction = -1;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                        direction = 1;
                    }
                    child.setTranslationX(direction * parallaxDelta);
                } else {
                    child.setTranslationY(-parallaxDelta);
                }
            }
        }
        //必须先由onMeasure调用，来存储测量的宽高
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height);
        } else {
            setMeasuredDimension(width, height - expansionDelta);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (animator != null) {
            animator.cancel();
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 获得扩展状态
     *
     * @return one of {@link State}
     */
    public int getState() {
        return state;
    }

    public boolean isExpanded() {
        return state == EXPANDING || state == EXPANDED;
    }

    /**
     * 设置展开，折叠的开关
     */
    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isExpanded()) {
            collapse(animate);
        } else {
            expand(animate);
        }
    }

    /**
     * 展开（有动画）
     */
    public void expand() {
        expand(true);
    }

    public void expand(boolean animate) {
        setExpanded(true, animate);
    }

    /**
     * 收回（有动画）
     */
    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animate) {
        setExpanded(false, animate);
    }

    /**
     * 便捷方法 - 与调用setExpanded（expanded，true）相同
     */
    public void setExpanded(boolean expand) {
        setExpanded(expand, true);
    }

    public void setExpanded(boolean expand, boolean animate) {
        if (expand == isExpanded()) {
            return;
        }

        int targetExpansion = expand ? 1 : 0;
        if (animate) {
            animateSize(targetExpansion);
        } else {
            setExpansion(targetExpansion);
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getExpansion() {
        return expansion;
    }

    /**
     * 设置展开度
     *
     * @param expansion (设置的值)
     */
    public void setExpansion(float expansion) {
        if (this.expansion == expansion) {
            return;
        }

        // 根据之前的值来推断状态
        float delta = expansion - this.expansion;
        if (expansion == 0) {
            state = COLLAPSED;
        } else if (expansion == 1) {
            state = EXPANDED;
        } else if (delta < 0) {
            state = COLLAPSING;
        } else if (delta > 0) {
            state = EXPANDING;
        }

        setVisibility(state == COLLAPSED ? GONE : VISIBLE);
        this.expansion = expansion;
        requestLayout();

        if (listener != null) {
            listener.onExpansionUpdate(expansion, state);
        }
    }

    public float getParallax() {
        return parallax;
    }

    public void setParallax(float parallax) {
        //
        parallax = Math.min(1, Math.max(0, parallax));
        this.parallax = parallax;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation < 0 || orientation > 1) {
            throw new IllegalArgumentException("Orientation must be either 0 (horizontal) or 1 (vertical)");
        }
        this.orientation = orientation;
    }

    public void setOnExpansionUpdateListener(OnExpansionUpdateListener listener) {
        this.listener = listener;
    }

    private void animateSize(int targetExpansion) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }

        animator = ValueAnimator.ofFloat(expansion, targetExpansion);
        animator.setInterpolator(interpolator);
        animator.setDuration(duration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setExpansion((float) valueAnimator.getAnimatedValue());
            }
        });

        animator.addListener(new ExpansionListener(targetExpansion));

        animator.start();
    }

    public interface OnExpansionUpdateListener {
        /**
         * 回调展开的监听
         *
         * @param expansionFraction 扩展是0-1之间的值
         * @param state             当前扩展的状态{@link State}
         */
        void onExpansionUpdate(float expansionFraction, int state);
    }

    private class ExpansionListener implements Animator.AnimatorListener {
        private int targetExpansion;
        private boolean canceled;

        public ExpansionListener(int targetExpansion) {
            this.targetExpansion = targetExpansion;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            state = targetExpansion == 0 ? COLLAPSING : EXPANDING;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!canceled) {
                state = targetExpansion == 0 ? COLLAPSED : EXPANDED;
                setExpansion(targetExpansion);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            canceled = true;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
