package com.example.mirek.diabetapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Mirek on 25.06.2018.
 */

public class Notification_receiver extends BroadcastReceiver{

    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;


    @Override
    public void onReceive(Context context,Intent intent){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context,MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("DiabetApp")
                .setContentText("Pamiętaj o zbadaniu poziomu cukru!")
                .setStyle(new NotificationCompat.BigTextStyle(). bigText("Pamiętaj, aby przed każdym posiłkiem sprawdzać poziom cukru we krwi"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(uri);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


    }


}
