package com.capstone.mycloset;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ImageCheckActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_select);
        Drawable image = getResources().getDrawable((int) getIntent().getExtras().get("Image"));

        ImageView imageView = (ImageView) findViewById(R.id.camera_img);
        imageView.setBackgroundColor(Color.GRAY);
        imageView.setImageDrawable(image);

        TextView textView = (TextView) findViewById(R.id.camera_btn);
        textView.setText("닫기");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
