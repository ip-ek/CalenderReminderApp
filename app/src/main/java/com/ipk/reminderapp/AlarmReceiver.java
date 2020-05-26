package com.ipk.reminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Çalıyor!", Toast.LENGTH_LONG).show();

        sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //seçilen alarm tonu çalar
        String alarmStr=sharedPreferences.getString("ringTone", alarmUri.toString());
        Uri alarm=Uri.parse(alarmStr);

        /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }*/
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarm);
        ringtone.play();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        buildNotification(context);

    }

    private void buildNotification(Context context) {

        NotificationCompat.Builder builder;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent ıntent = new Intent(context,UpcomingActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,ıntent,PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = "kanalId";
        String channelName = "kanalAd";
        String channelDesc = "kanalTanım";
        int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);

        if (channel == null) {
            channel = new NotificationChannel(channelId, channelName, kanalOnceligi);
            channel.setDescription(channelDesc);
            notificationManager.createNotificationChannel(channel);
        }

        builder = new NotificationCompat.Builder(context, channelId);

        builder.setContentTitle("Alarm Çaldı")  // gerekli
                .setContentText("Etkinlik gerçekleşti")  // gerekli
                .setSmallIcon(R.drawable.event_check) // gerekli
                .setAutoCancel(true)  // Bildirim tıklandıktan sonra kaybolur."
                .setContentIntent(pendingIntent);


        notificationManager.notify(1,builder.build());

    }
}
