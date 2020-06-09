package com.chad.baserecyclerviewadapterhelper.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public class DialogImpl {

    private Context mContext;

    protected Dialog mDialog;
    protected Window mWindow;

    private boolean mHovering;
    private boolean mShowing;
    protected final H mHandler = new H();

    public DialogImpl(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mWindow = mDialog.getWindow();
    }

    protected void initDialog(int windowType, WindowManager.LayoutParams lp, Class mClass) {
        mHovering = false;
        mShowing = false;
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//        mWindow.clearFlags(/*WindowManager.LayoutParams.FLAG_DIM_BEHIND
//                | */WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
//        mWindow.addFlags(/*WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | */WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mWindow.setType(windowType);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        mWindow.setAttributes(lp);
        mWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected final class H extends Handler {
        public static final int SHOW = 1;
        public static final int DISMISS = 2;
        public static final int UPDATE = 3;

        //        获取主线程looper 从而来做ui修改
        public H() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    showH();
                    break;
                case DISMISS:
                    dismissH();
                    break;
                case UPDATE:
                    break;
                default:
            }
        }
    }

    private void dismissH() {
        mDialog.dismiss();
    }

    private void showH() {
        mDialog.show();
    }

    public boolean dialogIsShow() {
        if (mDialog != null && mDialog.isShowing()) {
            return true;
        }
        return false;
    }

}
