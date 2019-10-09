package com.example.androidsocketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public static  final String TAG = "MainActivity";

    TextView text01;

    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text01 = (TextView) findViewById(R.id.text01);

        Button request_Button = (Button) findViewById(R.id.requestButton);
        request_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestThread thread = new RequestThread();
                thread.start();
            }
        });

        handler = new Handler();
    }

    class RequestThread extends Thread {
        public void run() {
            request();
        }

        private void request() {

            try {
                Socket socket = new Socket("172.30.1.25", 5001);
                appendText("client started : 172.30.1.25, 5001");

                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                outstream.writeUTF("Hello");
                outstream.flush();

                appendText("Hello sent.");

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
                String inStr = instream.readUTF();

                appendText( "inStr from server : " + inStr);

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private  void appendText(String msg) {
            final String inMsg = msg;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    text01.append(inMsg + "\n");
                }
            });
        }

    }
}
