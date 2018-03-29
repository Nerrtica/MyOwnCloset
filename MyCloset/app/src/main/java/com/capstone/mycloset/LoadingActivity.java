package com.capstone.mycloset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LoadingActivity extends FragmentActivity {
    private Runnable loadFinishRunnable;
    private Handler loadingHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

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
