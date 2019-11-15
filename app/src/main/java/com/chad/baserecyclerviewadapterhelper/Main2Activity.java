package com.chad.baserecyclerviewadapterhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Main2Activity extends AppCompatActivity {
    private static final String SETTING_KEY_MERGE_GROUP = "key_notify_merge_group";
    private boolean isGroupView;
    private Handler mSubHandler;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mContext = this;
        isGroupView = Settings.System.getInt(this.getContentResolver(), SETTING_KEY_MERGE_GROUP, 0) != 0;
        Switch aSwitch = findViewById(R.id.switch2);
        Button start = findViewById(R.id.bt_start);
        Button stop = findViewById(R.id.bt_stop);

        findViewById(R.id.switch_time).setOnClickListener(v -> {
            Settings.System.putString(mContext.getContentResolver(),
                    Settings.System.TIME_12_24, "12");
        });

        findViewById(R.id.switch_time_24).setOnClickListener(v -> {
            Settings.System.putString(mContext.getContentResolver(),
                    Settings.System.TIME_12_24, "24");
        });

        start.setOnClickListener(v -> {
            Intent start1 = new Intent(mContext, MyForegroundService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(start1);
            } else {
                startService(start1);
            }
        });
        stop.setOnClickListener(v -> {
            Intent stop1 = new Intent(mContext, MyForegroundService.class);
            stopService(stop1);
        });
        aSwitch.setChecked(isGroupView);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Settings.System.putInt(getContentResolver(), SETTING_KEY_MERGE_GROUP, 1);
                } else {
                    Settings.System.putInt(getContentResolver(), SETTING_KEY_MERGE_GROUP, 0);
                }
            }
        });


        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        mSubHandler = new Handler(handlerThread.getLooper(), mSubCallback);
        Message obtain = Message.obtain();
        obtain.arg1 = 0;
        obtain.setAsynchronous(true);
        mSubHandler.sendMessage(obtain);
    }

    private Handler.Callback mSubCallback = new Handler.Callback() {
        //该接口的实现就是处理异步耗时任务的，因此该方法执行在子线程中
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Log.e("kim", " isMainThread" + isMainThread());
                    break;

                default:
                    break;
            }

            return false;
        }
    };

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}
