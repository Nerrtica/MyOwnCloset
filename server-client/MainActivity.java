package com.example.caucse.client;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button sendDataButton;
    private EditText inputText;
    private TextView outputText;
    final int PORT = 11559;
    final String IP = "165.194.17.127";
    //DataInputStream is;
    //DataOutputStream os;
    public SendData connectionServer;
    //String message="";
    //Socket socket;
    DatagramSocket socket;
    Socket Socket;
    BufferedReader inputStream;
    PrintWriter outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendDataButton = (Button)findViewById(R.id.sendButton);
        inputText = (EditText) findViewById(R.id.TextField);
        outputText = (TextView)findViewById(R.id.outputField);


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
                outputStream = new PrintWriter(Socket.getOutputStream(),true);

                System.out.println("now 3");
                String message = inputText.getText().toString();

                outputStream.println(message);
                inputStream = new BufferedReader(new InputStreamReader(Socket.getInputStream()));

                message = inputStream.readLine();
                outputText.setText(message);

            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }


        }
    }
}
