package com.example.tanvisingh.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import java.net.*;
import java.io.*;
import java.lang.*;


public class MainActivity extends AppCompatActivity {

    String ip = "http://openweathermap.org/";
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        

        text = (TextView) findViewById(R.id.textView2);
        try
        {
            URL url = new URL(ip);
            executeReq(url);

            text.setText("HttpURLConnection Available");
        }
        catch(Exception e)
        {
            System.out.print(e);

        }

    }
    private void executeReq(URL url) throws IOException {
        // TODO Auto-generated method stub

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setReadTimeout(3000);
        con.setConnectTimeout(3500);
        con.setRequestMethod("GET");
        con.setDoInput(true);

        // Connect
        con.connect();
    }
}
