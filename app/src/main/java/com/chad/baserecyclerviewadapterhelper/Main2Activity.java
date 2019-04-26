package com.chad.baserecyclerviewadapterhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final View rootView = findViewById(R.id.item_root_view);
        final View imageLayout = findViewById(R.id.rl_icon_pkg);
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
