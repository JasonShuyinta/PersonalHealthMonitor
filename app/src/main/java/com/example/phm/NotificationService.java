package com.example.phm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class NotificationService extends BroadcastReceiver {
    DatabaseHelper db;
    String CHANNEL_ID = "200";
    String KEY_TEXT_REPLY = "key_text_reply";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        db = new DatabaseHelper(context);
        String bodyTemp, bloodPressure, bodyWeight, heartRate, glycemicIndex, sleepTime;
        bodyTemp = "body_temp_table";
        bloodPressure = "blood_pressure_table";
        bodyWeight = "body_weight_table";
        glycemicIndex = "glycemic_index_table";
        heartRate = "heart_rate_table";
        sleepTime = "sleep_time_table";

        //verify whether if user as inserted any record today
        long reports = db.countRows(bodyTemp) + db.countRows(bloodPressure)
                + db.countRows(bodyWeight) + db.countRows(glycemicIndex)
                +db.countRows(heartRate) + db.countRows(sleepTime);

        //builder for the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        //as the user clicks on the intent, open MainActivity aka ReportFragment
        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, myIntent, PendingIntent.FLAG_ONE_SHOT
        );

        /////////////////////////////////////////////////////
        /*reply
        String replyLabel = "Postpone reminder";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent resultIntent = new Intent(context, SettingsFragment2.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Notification.Action action = new Notification.Action.Builder(
        //        R.drawable.ic_menu_send, "SET TIME", resultPendingIntent)
        //        .addRemoteInput(remoteInput)
        //        .build(); */
        ////////////////////////////////////////////////////////

        //set notification features
          builder.setContentTitle("Daily Reminder")
                .setContentText("Remember to fill today's report")
                .setSmallIcon(R.drawable.alarmclock)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                //.addAction(R.drawable.ic_menu_send, "SET TIME", resultPendingIntent)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //if no reports are recorded, trigger notification
        if(reports == 0)
            notificationManager.notify(1, builder.build());

    }
}
