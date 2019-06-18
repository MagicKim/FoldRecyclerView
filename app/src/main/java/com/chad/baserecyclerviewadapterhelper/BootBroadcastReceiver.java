package com.chad.baserecyclerviewadapterhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by ${Kim} on 19-6-18.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    private static final String SETTING_KEY_MERGE_GROUP = "key_notify_merge_group";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            try {
                int anInt = Settings.System.getInt(context.getContentResolver(), SETTING_KEY_MERGE_GROUP);
                Log.e(TAG, "anInt = " + anInt);
            } catch (Exception e) {
                Log.w(TAG, "put uri value ");
                Settings.System.putInt(context.getContentResolver(), SETTING_KEY_MERGE_GROUP, 1);
            }
        }
    }
}
