package embedded.cse.cau.ac.kr.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button sendData;
    private EditText inputText;
    private TextView outputText;
    final int PORT = 1573;
    final String IP = "192.168.1.57";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendData = (Button)findViewById(R.id.sendButton);
        inputText = (EditText) findViewById(R.id.TextField);
        outputText = (TextView)findViewById(R.id.outputField);

        sendData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });
    }
    class SendData extends Thread{
        public void run(){
            try{
                Socket clientScoekt = new Socket(IP,PORT);
                clientScoekt.bind();

            } catch (IOException e) {
                System.out.println("ERROR");
                e.printStackTrace();
            }
        }
    }
}
