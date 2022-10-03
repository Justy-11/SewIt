package com.example.sewit.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.sewit.AcceptedReqFragment;
import com.example.sewit.MainActivity;
import com.example.sewit.OrderStatusFragment;
import com.example.sewit.OrdersFragment;
import com.example.sewit.R;
import com.example.sewit.TailorActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        //if(remoteMessage.getNotification() != null){
        //}
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String text = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();

        Intent intent = null;
        switch (Objects.requireNonNull(click_action)) {
            case "AcceptedReqFragment":
            case "OrderStatusFragment": {
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            }
            case "OrdersFragment": {
                intent = new Intent(this, TailorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            }
        }

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "sewIt";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Android O and higher need notification channel
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Request notification",NotificationManager.IMPORTANCE_HIGH);

            // configure notification channel
            notificationChannel.setDescription("sewIt channel for app test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Hearty365")
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo("info")
                .setContentIntent(pendingIntent);

        notificationManager.notify(1,notificationBuilder.build());

        super.onMessageReceived(remoteMessage);

    }


    @Override
    public void onNewToken(@NonNull String token) {

        //Log.d(TAG, "Refreshed token: " + token);

    }

}
