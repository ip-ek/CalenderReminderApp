package com.ipk.reminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.Context.ALARM_SERVICE;

public class UpcomeEventDao {
    private int id;

    public int addEvent(UpcomeEventDatabase db, UpcomeEvent event){
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        ContentValues values= new ContentValues();   //veritanına verilerin yazılması için gerekn değerleri tutar.
        values.put("type", event.getType());
        values.put("label", event.getLabel());
        values.put("content", event.getContent());
        values.put("startDate", event.getStartDate());
        values.put("startTime", event.getStartTime());
        values.put("endDate", event.getEndDate());
        values.put("endTime", event.getEndTime());
        values.put("remindTime", event.getRemindTime());
        values.put("eventFreq", event.getEnventFreq());
        values.put("address", event.getAddress());
        values.put("parentEvent", event.getParent());
        values.put("counter", event.getCounter());
        values.put("alarm", event.getAlarm());
        id=(int)sqLiteDatabase.insertOrThrow("events",null, values);
        sqLiteDatabase.close();

        if(id==-1)  return -1;
        else return id;
    }

    public ArrayList<UpcomeEvent> getAllEvents(UpcomeEventDatabase db){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events", null);

        while (cursor.moveToNext()){
            UpcomeEvent event =new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("label")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("startDate")),
                    cursor.getString(cursor.getColumnIndex("startTime")),
                    cursor.getString(cursor.getColumnIndex("endDate")),
                    cursor.getString(cursor.getColumnIndex("endTime")),
                    cursor.getString(cursor.getColumnIndex("remindTime")),
                    cursor.getInt(cursor.getColumnIndex("eventFreq")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getInt(cursor.getColumnIndex("parentEvent")),
                    cursor.getInt(cursor.getColumnIndex("counter")),
                    cursor.getString(cursor.getColumnIndex("alarm")));
            Log.d("takip", "label: "+ cursor.getString(cursor.getColumnIndex("label")));
            eventArrayList.add(event);
        }

        return eventArrayList;
    }

    public UpcomeEvent getEvent(UpcomeEventDatabase db, int id){
        UpcomeEvent event=new UpcomeEvent();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events WHERE event_id="+id, null);
        while(cursor.moveToNext()){
            UpcomeEvent event2 =new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("label")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("startDate")),
                    cursor.getString(cursor.getColumnIndex("startTime")),
                    cursor.getString(cursor.getColumnIndex("endDate")),
                    cursor.getString(cursor.getColumnIndex("endTime")),
                    cursor.getString(cursor.getColumnIndex("remindTime")),
                    cursor.getInt(cursor.getColumnIndex("eventFreq")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getInt(cursor.getColumnIndex("parentEvent")),
                    cursor.getInt(cursor.getColumnIndex("counter")),
                    cursor.getString(cursor.getColumnIndex("alarm")));
            event=event2;
        }
        return event;
    }

    public ArrayList<UpcomeEvent> getDailyEvents(UpcomeEventDatabase db, String date, ArrayList<Integer> typeFilter){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events", null);

        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("startDate")).equals(date) &&
                    typeFilter.contains(cursor.getInt(cursor.getColumnIndex("type")))) {
                UpcomeEvent event = new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                        cursor.getInt(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("label")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("startDate")),
                        cursor.getString(cursor.getColumnIndex("startTime")),
                        cursor.getString(cursor.getColumnIndex("endDate")),
                        cursor.getString(cursor.getColumnIndex("endTime")),
                        cursor.getString(cursor.getColumnIndex("remindTime")),
                        cursor.getInt(cursor.getColumnIndex("eventFreq")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getInt(cursor.getColumnIndex("parentEvent")),
                        cursor.getInt(cursor.getColumnIndex("counter")),
                        cursor.getString(cursor.getColumnIndex("alarm")));
                Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                eventArrayList.add(event);
            }
        }
        return eventArrayList;
    }

    public ArrayList<UpcomeEvent> getMonthEvents(UpcomeEventDatabase db, String date, ArrayList<Integer> typeFilter){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events", null);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar start = Calendar.getInstance();
        Calendar end= Calendar.getInstance();
        String dbDate;
        try {
            start.setTime(format.parse(date));
            end.setTime(format.parse(date));
            end.add(Calendar.MONTH,1);
            //TODO: kod tekrarı var hafta ile ://
            while (cursor.moveToNext()) {
                //dd/mm/yyyy
                dbDate=cursor.getString(cursor.getColumnIndex("startDate"));
                if ((dbDate.equals(date) ||(format.parse(dbDate).after(start.getTime()) && format.parse(dbDate).before(end.getTime()))) &&
                        typeFilter.contains(cursor.getInt(cursor.getColumnIndex("type")))) {
                    UpcomeEvent event = new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                            cursor.getInt(cursor.getColumnIndex("type")),
                            cursor.getString(cursor.getColumnIndex("label")),
                            cursor.getString(cursor.getColumnIndex("content")),
                            cursor.getString(cursor.getColumnIndex("startDate")),
                            cursor.getString(cursor.getColumnIndex("startTime")),
                            cursor.getString(cursor.getColumnIndex("endDate")),
                            cursor.getString(cursor.getColumnIndex("endTime")),
                            cursor.getString(cursor.getColumnIndex("remindTime")),
                            cursor.getInt(cursor.getColumnIndex("eventFreq")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getInt(cursor.getColumnIndex("parentEvent")),
                            cursor.getInt(cursor.getColumnIndex("counter")),
                            cursor.getString(cursor.getColumnIndex("alarm")));
                    Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                    eventArrayList.add(event);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return eventArrayList;
    }

    public ArrayList<UpcomeEvent> getWeekEvents(UpcomeEventDatabase db, String date, ArrayList<Integer> typeFilter){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events", null);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar start = Calendar.getInstance();
        Calendar end= Calendar.getInstance();
        String dbDate;
        try {
            start.setTime(format.parse(date));
            end.setTime(format.parse(date));
            end.add(Calendar.DATE,7);

            while (cursor.moveToNext()) {
                //dd/mm/yyyy
                dbDate=cursor.getString(cursor.getColumnIndex("startDate"));
                if ((dbDate.equals(date) ||(format.parse(dbDate).after(start.getTime()) && format.parse(dbDate).before(end.getTime()))) &&
                        typeFilter.contains(cursor.getInt(cursor.getColumnIndex("type")))) {
                    UpcomeEvent event = new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                            cursor.getInt(cursor.getColumnIndex("type")),
                            cursor.getString(cursor.getColumnIndex("label")),
                            cursor.getString(cursor.getColumnIndex("content")),
                            cursor.getString(cursor.getColumnIndex("startDate")),
                            cursor.getString(cursor.getColumnIndex("startTime")),
                            cursor.getString(cursor.getColumnIndex("endDate")),
                            cursor.getString(cursor.getColumnIndex("endTime")),
                            cursor.getString(cursor.getColumnIndex("remindTime")),
                            cursor.getInt(cursor.getColumnIndex("eventFreq")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getInt(cursor.getColumnIndex("parentEvent")),
                            cursor.getInt(cursor.getColumnIndex("counter")),
                            cursor.getString(cursor.getColumnIndex("alarm")));
                    Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                    eventArrayList.add(event);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return eventArrayList;
    }

    public void deleteEvent(UpcomeEventDatabase db, int eventID, Context context){
        cancelAlarm(getEvent(db, eventID).getAlarm(), context);
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        sqLiteDatabase.delete("events", "event_id=?", new String[]{String.valueOf(eventID)});
        deleteRepeatedEvent(db, eventID, context);
        sqLiteDatabase.close();
    }

    public ArrayList<UpcomeEvent> getEventByParent(UpcomeEventDatabase db, int id){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events WHERE parentEvent="+id, null);
        while(cursor.moveToNext()){
            UpcomeEvent event =new UpcomeEvent(cursor.getInt(cursor.getColumnIndex("event_id")),
                    cursor.getInt(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("label")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("startDate")),
                    cursor.getString(cursor.getColumnIndex("startTime")),
                    cursor.getString(cursor.getColumnIndex("endDate")),
                    cursor.getString(cursor.getColumnIndex("endTime")),
                    cursor.getString(cursor.getColumnIndex("remindTime")),
                    cursor.getInt(cursor.getColumnIndex("eventFreq")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getInt(cursor.getColumnIndex("parentEvent")),
                    cursor.getInt(cursor.getColumnIndex("counter")),
                    cursor.getString(cursor.getColumnIndex("alarm")));
            eventArrayList.add(event);
        }
        return eventArrayList;
    }

    public void deleteRepeatedEvent(UpcomeEventDatabase db, int parentID, Context context){
        ArrayList<UpcomeEvent> eventArrayList = getEventByParent(db, parentID);
        for(int i=0; i<eventArrayList.size(); i++){
            cancelAlarm(eventArrayList.get(i).getAlarm(),context);
        }
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        sqLiteDatabase.delete("events", "parentEvent=?", new String[]{String.valueOf(parentID)});
        sqLiteDatabase.close();
    }

    public void updateEvent(UpcomeEventDatabase db, int eventID, UpcomeEvent event, int parent, Context context){
        cancelAlarm(getEvent(db, eventID).getAlarm(),context);

        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        ContentValues values= new ContentValues();   //veritanına verilerin yazılması için gerekn değerleri tutar.
        values.put("type", event.getType());
        values.put("label", event.getLabel());
        values.put("content", event.getContent());
        values.put("startDate", event.getStartDate());
        values.put("startTime", event.getStartTime());
        values.put("endDate", event.getEndDate());
        values.put("endTime", event.getEndTime());
        values.put("remindTime", event.getRemindTime());
        values.put("eventFreq", event.getEnventFreq());
        values.put("address", event.getAddress());
        values.put("parentEvent", parent);
        values.put("counter", event.getCounter());
        sqLiteDatabase.update("events",values, "event_id=?", new String[]{String.valueOf(eventID)});
        sqLiteDatabase.close();
    }

    private void cancelAlarm(String codes, Context context){
        Log.d("alarmm", "geldi");
        if(!codes.equals("")){
            Log.d("alarmm", codes);
            String[] codeArr=codes.split(",");
            for(int i=0; i<codeArr.length; i++){
                Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), Integer.valueOf(codeArr[i]), intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
        }
    }

}
