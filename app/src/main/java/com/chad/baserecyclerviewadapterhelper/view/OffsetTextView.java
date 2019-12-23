package com.chad.baserecyclerviewadapterhelper.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by  on 19-12-2.
 */
public class OffsetTextView extends TextView {
    public OffsetTextView(Context context) {
        super(context);
    }

    public OffsetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OffsetTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("CustomTextView", "Before X:" + getX() + " Y:" + getY()
                        + " Left:" + getLeft() + " Top:" + getTop()
                        + " Right:" + getRight() + " Bottom:" + getBottom());

                // layout
//                layout(getLeft() + 100, getTop() + 100, getRight() + 100, getBottom() + 100);

                // offsetLeftAndRight、offsetTopAndBottom
                offsetLeftAndRight(-100);
//                offsetTopAndBottom(100);

                // setLayoutParams
//                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
//                lp.leftMargin = getLeft() + 100;
//                lp.topMargin = getTop() + 100;
//                setLayoutParams(lp);

                // scrollTo scrollBy
//                scrollTo(-100, -100);
//                ((View) getParent()).scrollTo(-100, -100);
//                ((View) getParent()).scrollBy(-100, -100);

                // 属性动画
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.playTogether(
//                        ObjectAnimator.ofFloat(this, "translationX", 100),
//                        ObjectAnimator.ofFloat(this, "translationY", 100)
//                );
//                animatorSet.start();

                // 位移动画
//                TranslateAnimation anim = new TranslateAnimation(0, 100, 0, 100);
//                anim.setFillAfter(true);
//                startAnimation(anim);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("CustomTextView", "After X:" + getX() + " Y:" + getY()
                                + " Left:" + getLeft() + " Top:" + getTop() + " Right:" + getRight() + " Bottom:" + getBottom());
                    }
                }, 3000);
                break;
        }
        return false;
    }
}
