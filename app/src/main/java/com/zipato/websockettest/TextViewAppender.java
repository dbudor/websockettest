package com.zipato.websockettest;


import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

public class TextViewAppender extends WebSocketClient {
    private int OPEN = Color.parseColor("#ff8c00");
    private int MESSAGE = Color.parseColor("#2222aa");
    private int CLOSE = Color.parseColor("#77aa77");
    private int ERROR = Color.parseColor("#bb0000");

    final Handler handler;
    final TextView view;

    public TextViewAppender(Handler handler, TextView view, URI uri) {
        super(uri);
        this.handler = handler;
        this.view = view;
    }

    public void onOpen(ServerHandshake handshakedata) {
        append("" + handshakedata.getHttpStatus() + " " + handshakedata.getHttpStatusMessage(), OPEN);
    }

    public void onMessage(String message) {
        append(message, MESSAGE);
    }

    public void onClose(int code, String reason, boolean remote) {
        append("" + code + " " + reason + ", remote=" + remote, CLOSE);
    }

    public void onError(Exception ex) {
        append(ex.getClass().getSimpleName() + ": " + ex.getMessage(), ERROR);
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        append(sw.toString(), ERROR);
    }

    private void append(String s, int color) {
        handler.post(() -> {
            CharSequence text = view.getText();
            if (text.length() > 8000) {
                String t = text.toString();
                int idx = t.indexOf("\n", 7600);
                if (idx > 0) {
                    view.setText(t.substring(idx));
                }
            }
            view.append("\n");
            Spannable wordtoSpan = new SpannableString(s);
            wordtoSpan.setSpan(new ForegroundColorSpan(color), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.append(wordtoSpan);
        });
    }

}
