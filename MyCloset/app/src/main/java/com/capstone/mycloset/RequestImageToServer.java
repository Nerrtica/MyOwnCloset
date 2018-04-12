package com.capstone.mycloset;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;

public class RequestImageToServer extends FragmentActivity {
    private Bitmap photo;
    private RequestImageToServer activity;

    private final int PORT = 11559;
    private final String IP = "114.205.31.136";

    private SendData connectionServer;
    private Socket Socket;

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        activity = this;
        Uri photoUri = (Uri) getIntent().getExtras().get("Image");
        photo = null;
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
                connectionServer= new SendData(photo);
                connectionServer.start();
            }
        });
    }

    class SendData extends Thread {
        private Bitmap image;

        public SendData(Bitmap bitmap) {
            this.image = bitmap;
        }

        public void run(){
            try{
//                System.out.println("now 2");
                Socket = new Socket(IP,PORT);
                //socket = new DatagramSocket();
                InputStream inputStream1 = Socket.getInputStream();
                OutputStream outputStream = Socket.getOutputStream();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,10,stream);
                byte[] byteArray = stream.toByteArray();
                outputStream.write(byteArray);
                outputStream.write("end".getBytes());

//                System.out.println("now 3");

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                int byteRead;
//                outputStream.println();
                if((byteRead = inputStream1.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer,0,byteRead);
                    result = "Result Code : " + byteArrayOutputStream.toString("UTF-8");
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            } catch (IOException e) {
//                System.out.println("ERROR");
                e.printStackTrace();
            }

        }
    }
}
