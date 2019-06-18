package com.chad.baserecyclerviewadapterhelper;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Main2Activity extends AppCompatActivity {
    private static final String SETTING_KEY_MERGE_GROUP = "key_notify_merge_group";
    private boolean isGroupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final View rootView = findViewById(R.id.item_root_view);
        final View imageLayout = findViewById(R.id.rl_icon_pkg);

        isGroupView = Settings.System.getInt(this.getContentResolver(), SETTING_KEY_MERGE_GROUP, 0) != 0;
        Switch aSwitch = findViewById(R.id.switch2);
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

        final ViewTreeObserver vto = rootView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int height = rootView.getMeasuredHeight();
                int imageLayoutHeight = imageLayout.getMeasuredHeight();
                Log.v("kim", "toot view height = " + height);
                Log.v("kim", "toot view height = " + imageLayoutHeight);
                return true;
            }
        });

    }
}
