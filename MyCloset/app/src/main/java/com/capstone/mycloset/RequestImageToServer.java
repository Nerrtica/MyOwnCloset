package com.capstone.mycloset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class RequestImageToServer extends FragmentActivity {
    private Bitmap photo;
    private RequestImageToServer activity;

    private ProgressBar progressBar;

    private int PORT;
    private String IP;

    private SendData connectionServer;
    private Socket Socket;

    private String result;
    private String gender;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        IP = preferences.getString("key_ip", "");
        PORT = new Integer(preferences.getString("key_port", "5000"));

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        gender = preferences.getString("key_gender", "-1");

        activity = this;
        photoUri = (Uri) getIntent().getExtras().get("Image");
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
                if(progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                    connectionServer = new SendData(photo);
                    connectionServer.start();
//                    test();
                }
            }
        });
    }

    void test() {
        DBController controller ;
        controller = new DBController(getApplicationContext());
        for(int i = 0; i < 15; i++) {
            controller.InsertCloset(i, 1, 1, 1, photoUri.toString());
            controller.InsertCloset(i, 1, 1, 1, photoUri.toString());
            controller.InsertCloset(i, 1, 1, 1, photoUri.toString());
            controller.InsertCloset(i, 1, 1, 1, photoUri.toString());
            controller.InsertCloset(i, 1, 1, 1, photoUri.toString());
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                ClosetActivity.refreshCloset = true;
                finish();
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
                Socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(IP, PORT);
                Socket.setSoTimeout(3000);
                Socket.connect(socketAddress, 3000);
//                if(!Socket.isConnected()){
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
////                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getApplicationContext(), "Server Error\n 서버를 확인해 주세요", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    });
//                }
                //socket = new DatagramSocket();
                InputStream inputStream = Socket.getInputStream();
                OutputStream outputStream = Socket.getOutputStream();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] byteArray = stream.toByteArray();

                // Make ByteArray Form : Gender;ImageByteArray
                gender = gender + ";";
                byte[] temp = gender.getBytes();
                byte[] finalByteArray = new byte[byteArray.length + temp.length];
                System.arraycopy(temp, 0, finalByteArray, 0, temp.length);
                System.arraycopy(byteArray, 0, finalByteArray, temp.length, byteArray.length);

                outputStream.write(finalByteArray);
                outputStream.write("end".getBytes());

                // Get Server Text
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                int byteRead;
                if((byteRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer,0,byteRead);
                    result = byteArrayOutputStream.toString("UTF-8");
                    try {
                        JSONObject json = new JSONObject(result);
                        // Json Parsing Process
                        DBController controller ;
                        controller = new DBController(getApplicationContext());
                        controller.InsertCloset(json.getInt("category"), json.getInt("color"), json.getInt("pattern"),
                                json.getInt("is_long"), photoUri.toString());
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
//                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                ClosetActivity.refreshCloset = true;
                                Intent intent = new Intent(getApplicationContext() , ResultActivity.class);
                                intent.putExtra("Result", result);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }
            } catch (SocketException e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
//                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Server Error\n 서버를 확인해 주세요", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (IOException e) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
//                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Server Error\n 서버를 확인해 주세요", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    }
}
