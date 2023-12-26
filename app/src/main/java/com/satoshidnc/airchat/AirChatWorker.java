package com.satoshidnc.airchat;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.neovisionaries.ws.client.*;

import java.io.IOException;

public class AirChatWorker extends Worker {
    public AirChatWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(AirChatWorker.class.getSimpleName(),"doWork()");

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new WebSocketFactory()
                .createSocket("wss://relay.satoshidnc.com")
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket ws, String message) {
                        // Received a response. Print the received message.
                        Log.i(AirChatWorker.class.getSimpleName(), message);

                        // Close the WebSocket connection.
                        ws.disconnect();
                    }
                })
                .connect()
                .sendText("Hello.");
        } catch (WebSocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Result.success();
    }
}
