package com.zipato.websockettest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends Activity {

    private final URI IP_URI = new URI("ws://127.0.0.1:8001/");
//    private final URI HOST_URI = new URI("ws://localhost:8001/");

    private TextView ws;
    private TextViewAppender rs;

    private Handler handler = new Handler();

    public MainActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ws = (TextView) findViewById(R.id.ws);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rs = new TextViewAppender(handler, ws, IP_URI);
        rs.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rs.close();
    }

}