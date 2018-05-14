package com.capstone.mycloset;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class LoadingActivity extends FragmentActivity {
    private Runnable loadFinishRunnable;
    private Handler loadingHandler;
    private DBOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        openHelper = new DBOpenHelper(this,"testDB.db",null,1);

//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        }

        loadFinishRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext() , ClosetActivity.class);
                startActivity(intent);
                finish();
            }
        };

        loadingHandler = new Handler();
        loadingHandler.postDelayed(loadFinishRunnable, 3000);
    }

    @Override
    public void onBackPressed() {    }
}
