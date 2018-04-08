package com.capstone.mycloset;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class RequestImageToServer extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        Uri photoUri = (Uri) getIntent().getExtras().get("Image");
        Bitmap photo = null;
        try {
            photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }

        ImageView imageView = (ImageView) findViewById(R.id.camera_img);
        imageView.setImageBitmap(photo);

        TextView textView = (TextView) findViewById(R.id.camera_btn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
