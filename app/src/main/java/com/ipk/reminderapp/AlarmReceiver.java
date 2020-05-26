package com.ipk.reminderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

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

    }
}
