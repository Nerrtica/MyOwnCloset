package com.example.caucse.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

                socket = new DatagramSocket();
                InetAddress serverAddr = InetAddress.getByName(IP);
                System.out.println("now 3");
                byte[] message = inputText.getText().toString().getBytes();
                DatagramPacket packet = new DatagramPacket(message,message.length,serverAddr,PORT);
                socket.send(packet);

                socket.receive((packet));
                String sentence = new String(packet.getData());

                outputText.setText(sentence);

            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }


        }
    }
}