package com.chad.baserecyclerviewadapterhelper.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.baserecyclerviewadapterhelper.R;

/**
 * 无线充电检测到金属异物弹框提醒
 */
public class CustomWarnDialog extends DialogImpl implements View.OnClickListener {

    private static final String TAG = CustomWarnDialog.class.toString();

    private Context mContext;

    private Button mWirelessFBWarnBt;
    private ImageView mCloseForeignBody;

    private int second = 5;

    public CustomWarnDialog(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
//                initDialog(WindowManager.LayoutParams.TYPE_VOLUME_OVERLAY,128,WTWirelessFBWarnDialog.class);
        //        initDialog(WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW+20,128,WTWirelessFBWarnDialog.class);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.format = PixelFormat.TRANSLUCENT;
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lp.y = 0;
        lp.windowAnimations = -1;
        lp.dimAmount = 1f;
        initDialog(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, lp, CustomWarnDialog.class);
    }

    @Override
    protected void initDialog(int windowType, WindowManager.LayoutParams lp, Class mClass) {
        super.initDialog(windowType, lp, mClass);
        mDialog.setContentView(R.layout.wt_wireless_charging_foreign_body_warn_dialog);
        mDialog.setCanceledOnTouchOutside(false);

        mWirelessFBWarnBt = mWindow.findViewById(R.id.wt_wireless_charging_foreign_body_warn_button);
        mCloseForeignBody = mWindow.findViewById(R.id.icon_foreign_body_close);
        mWirelessFBWarnBt.setOnClickListener(this);
        mCloseForeignBody.setOnClickListener(this);
    }

    public void showDialog() {
        Log.i(TAG, "showDialog second = " + second);
        if (second != 5) {
            return;
        }
        setWirelessFBWarnBtText(second);
        mHandler.sendEmptyMessage(H.SHOW);
        mHandler.postDelayed(runnable, 1000);
    }

    //设置5秒倒计时
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setWirelessFBWarnBtText(--second);
            if (second > 0) {
                mHandler.postDelayed(runnable, 1000);
            } else {
                mHandler.removeCallbacks(runnable);
                dismissDialog();
            }
        }
    };

    private void dismissDialog() {
        mHandler.removeCallbacks(runnable);
        mHandler.sendEmptyMessage(H.DISMISS);
        second = 5;
    }

    //动态设置文字加倒计时时间
    private void setWirelessFBWarnBtText(int second) {
        mWirelessFBWarnBt.setText(mContext.getString(R.string.wt_wireless_charging_foreign_body_warn_button_text, second));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wt_wireless_charging_foreign_body_warn_button:
                dismissDialog();
            case R.id.icon_foreign_body_close:
                dismissDialog();
                break;
        }
    }
}
