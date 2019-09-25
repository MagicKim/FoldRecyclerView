package com.chad.baserecyclerviewadapterhelper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class MyForegroundService extends Service {
    private static final String TAG = "MyService";
    private static final String ID = "channel_1";
    private static final String NAME = "前台服务";

    public MyForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        if (Build.VERSION.SDK_INT >= 26) {
            setForeground();
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(26)
    private void setForeground() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(null, null);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, ID)
                .setContentTitle("收到一条重要通知")
                .setContentText("这是重要通知")
                .setSmallIcon(R.drawable.icon_anguage)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_anguage))
                .build();
        startForeground(1, notification);
    }

}
