package com.chad.baserecyclerviewadapterhelper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.R;

/**
 * Created by ${Kim} on 19-6-5.
 */
public class SquareView extends View {

    private Paint mPaint;
    private Rect rect;

    //触摸事件相关的变量
    private static final int TOUCH_MOVE = 1;//移动
    private static final int TOUCH_SINGLE_CLICK = 2;//单击
    private static final int TOUCH_DOUBLE_CLICK = 3;//双击
    //一次单击事件由DOWN和UP两个事件合成，假设从down到up间隔小于200毫秒，我们就认为发生了一次单击事件
    private static final int singleClickDurationTime = 200;
    //一次双击事件由两个点击事件合成，两个单击事件之间小于300毫秒，我们就认为发生了一次双击事件
    private static final int doubleClickDurationTime = 300;
    private long lastSingleClickTime = -1;//上次发生单击的时刻
    private long touchDownTime = -1;//触点按下的时刻
    private long touchUpTime = -1;//触点弹起的时刻
    private float touchX = -1;//触点的x坐标
    private float touchY = -1;//触点的y坐标

    public SquareView(Context context) {
        super(context);
        init();
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        //设置textPaint，设置为抗锯齿
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_light_blue));
        rect = new Rect(100, 100, 300, 300);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        resolveTouchType(event);
        return true;
    }

    //合成我们想要的事件类型
    private int resolveTouchType(MotionEvent event){
        int touchType = -1;
        int action = event.getAction();
        touchX = event.getX();
        touchY = event.getY();
        if(action == MotionEvent.ACTION_DOWN){
            //触点按下
            touchDownTime = System.currentTimeMillis();
        }else if(action == MotionEvent.ACTION_UP){
            //触点弹起
            touchUpTime = System.currentTimeMillis();
            //计算触点按下到触点弹起之间的时间差
            long downUpDurationTime = touchUpTime - touchDownTime;
            //如果此次触点按下和抬起之间的时间差小于一次单击事件指定的时间差，
            //那么我们就认为发生了一次单击
            if(downUpDurationTime <= singleClickDurationTime){
                //计算这次单击距离上次单击的时间差
                long twoClickDurationTime = touchUpTime - lastSingleClickTime;

                if(twoClickDurationTime <=  doubleClickDurationTime){
                    //如果两次单击的时间差小于一次双击事件执行的时间差，
                    //那么我们就认为发生了一次双击事件
                    touchType = TOUCH_DOUBLE_CLICK;
                    Log.v("kim","双击-----");
                    //重置变量
                    lastSingleClickTime = -1;
                    touchDownTime = -1;
                    touchUpTime = -1;
                }else{
                    //如果这次形成了单击事件，但是没有形成双击事件，那么我们暂不触发此次形成的单击事件
                    //我们应该在doubleClickDurationTime毫秒后看一下有没有再次形成第二个单击事件
                    //如果那时形成了第二个单击事件，那么我们就与此次的单击事件合成一次双击事件
                    //否则在doubleClickDurationTime毫秒后触发此次的单击事件
                    lastSingleClickTime = touchUpTime;
                }
            }
        }
        return touchType;
    }

    //在onDraw方法中调用该方法，在每一帧都检查是不是发生了单击事件
    private boolean isSingleClick(){
        boolean singleClick = false;
        //我们检查一下是不是上次的单击事件在经过了doubleClickDurationTime毫秒后满足触发单击事件的条件
        if(lastSingleClickTime > 0){
            //计算当前时刻距离上次发生单击事件的时间差
            long deltaTime = System.currentTimeMillis() - lastSingleClickTime;

            if(deltaTime >= doubleClickDurationTime){
                //如果时间差超过了一次双击事件所需要的时间差，
                //那么就在此刻延迟触发之前本该发生的单击事件
                singleClick = true;
                //重置变量
                lastSingleClickTime = -1;
                touchDownTime = -1;
                touchUpTime = -1;
            }
        }
        return singleClick;
    }
}
