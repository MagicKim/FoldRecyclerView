package com.chad.baserecyclerviewadapterhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chad.baserecyclerviewadapterhelper.game.GameView;

public class Main4Activity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        gameView = findViewById(R.id.gameView);
        int[] bitmapIds = {
                R.drawable.plane,
                R.drawable.explosion,
                R.drawable.yellow_bullet,
                R.drawable.blue_bullet,
                R.drawable.small,
                R.drawable.middle,
                R.drawable.big,
                R.drawable.bomb_award,
                R.drawable.bullet_award,
                R.drawable.pause1,
                R.drawable.pause2,
                R.drawable.bomb
        };
        gameView.start(bitmapIds);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameView != null){
            gameView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameView != null){
            gameView.destroy();
        }
        gameView = null;
    }
}
