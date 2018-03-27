package embedded.cse.cau.ac.kr.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button sendDataButton;
    private EditText inputText;
    private TextView outputText;
    final int PORT = 11559;
    final String IP = "10.210.60.37";
    DataInputStream is;
    DataOutputStream os;
    public SendData connectionServer;
    String message="";

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
                 connectionServer= new SendData();
                 connectionServer.start();
            }
        });
    }
    class SendData extends Thread{
        public void run(){
            try{
                Socket clientScoekt = new Socket(IP,PORT);

                is=new DataInputStream(clientScoekt.getInputStream());
                os=new DataOutputStream(clientScoekt.getOutputStream());

            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }

            message = inputText.getText().toString();

            try {
                os.writeUTF(message);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                message = is.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    outputText.setText(message);
                }
            });

        }
    }
}
