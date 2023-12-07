package com.muslimlife.app.view.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.muslimlife.app.R;
import com.muslimlife.app.view.MainActivity;

public class MuslimFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification());
        } else {
            Log.e(TAG, "remoteMessage.getNotification() = null");
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }


    private void sendNotification(RemoteMessage.Notification notification) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID;
        if (notification.getChannelId() != null)
            CHANNEL_ID = notification.getChannelId();
        else
            CHANNEL_ID = "Default";
        Log.d(TAG, "sendNotification: " + CHANNEL_ID );

        NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);

        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel( mChannel );
        }

        NotificationCompat.Builder status = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        status.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_blue))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                   .setSound(mChannel.getSound())
                .setContentIntent(pendingIntent);
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, status.build());
        }


    }

}
