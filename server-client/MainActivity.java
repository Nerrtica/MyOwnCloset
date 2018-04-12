package com.example.caucse.a20101567_inclass;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button sendDataButton;
    private EditText inputText;
    private TextView outputText;
    final int PORT = 11559;
    final String IP = "114.205.31.136";
    //DataInputStream is;
    //DataOutputStream os;
    public SendData connectionServer;
    //String message="";
    //Socket socket;
    DatagramSocket socket;
    Socket Socket;
    BufferedReader inputStream;
    PrintWriter outputStream;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendDataButton = (Button)findViewById(R.id.sendButton);
        inputText = (EditText) findViewById(R.id.TextField);
        outputText = (TextView)findViewById(R.id.outputField);
        image = BitmapFactory.decodeResource(getResources(),R.drawable.lose);

        sendDataButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("button","clicked button");

                connectionServer= new SendData();
                connectionServer.start();
            }
        });
    }
    class SendData extends Thread{
        public void run(){
            try{
                System.out.println("now 2");
                Socket = new Socket(IP,PORT);
                //socket = new DatagramSocket();
                InputStream inputStream1 = Socket.getInputStream();
                OutputStream outputStream = Socket.getOutputStream();


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,10,stream);
                byte[] byteArray = stream.toByteArray();
                outputStream.write(byteArray);
                outputStream.write("end".getBytes());

                System.out.println("now 3");

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                int byteRead;
                //outputStream.println();
                if((byteRead = inputStream1.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer,0,byteRead);
                }

                outputText.setText(byteArrayOutputStream.toString("UTF-8"));
            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }

        }
    }
}
