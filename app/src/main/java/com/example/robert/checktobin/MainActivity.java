package com.example.robert.checktobin;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText editText = findViewById(R.id.editText);

                TextView textView =  findViewById(R.id.textView);

                textView.setVisibility(View.VISIBLE);


                connectandhandle(editText.getText().toString(), textView);

            }
        });
    }

    private void connectandhandle(final String input, final TextView textView){


        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Socket socket = new Socket("se2-isys.aau.at",53212);

                    byte[] message = input.getBytes();

                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                    writer.print(message);
                    writer.flush();


                    handler.post(new Runnable() {
                        @Override
                        public void run() {


                            if(input.length()<1)
                                textView.setText("Keine Eingabe!");
                            else
                                textView.setText("Quersumme der Matrikelnummer als Binärzahl: \n"+checksumtobinary(input));


                        }
                    });

                    writer.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        });

        thread.start();



    }

    private String checksumtobinary(String input){

        int checksum=0;

        for(int i=0;i<input.length();i++)
            checksum+=Character.getNumericValue(input.charAt(i));


        return Integer.toBinaryString(checksum);
    }

}
