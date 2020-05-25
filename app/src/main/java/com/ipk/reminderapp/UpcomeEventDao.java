package com.ipk.reminderapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpcomeEventDao {
    private int id;

    public int addEvent(UpcomeEventDatabase db, UpcomeEvent event, int parent){
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
                    cursor.getInt(cursor.getColumnIndex("parentEvent")));
            Log.d("takip", "label: "+ cursor.getString(cursor.getColumnIndex("label")));
            eventArrayList.add(event);
        }

        return eventArrayList;
    }

    public ArrayList<UpcomeEvent> getDailyEvents(UpcomeEventDatabase db, String date, String typeFilter){
        ArrayList<UpcomeEvent> eventArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM events", null);

        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("startDate")).equals(date)) {
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
                        cursor.getInt(cursor.getColumnIndex("parentEvent")));
                Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                eventArrayList.add(event);
            }
        }
        return eventArrayList;
    }

    public ArrayList<UpcomeEvent> getMonthEvents(UpcomeEventDatabase db, String date, String typeFilter){
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
                if (dbDate.equals(date) ||(format.parse(dbDate).after(start.getTime()) && format.parse(dbDate).before(end.getTime()))) {
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
                            cursor.getInt(cursor.getColumnIndex("parentEvent")));
                    Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                    eventArrayList.add(event);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return eventArrayList;
    }

    public ArrayList<UpcomeEvent> getWeekEvents(UpcomeEventDatabase db, String date, String typeFilter){
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
                if (dbDate.equals(date) ||(format.parse(dbDate).after(start.getTime()) && format.parse(dbDate).before(end.getTime()))) {
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
                            cursor.getInt(cursor.getColumnIndex("parentEvent")));
                    Log.d("takip", "label: " + cursor.getString(cursor.getColumnIndex("label")));
                    eventArrayList.add(event);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return eventArrayList;
    }

    public void deleteEvent(UpcomeEventDatabase db, int eventID){
        SQLiteDatabase sqLiteDatabase= db.getWritableDatabase();
        sqLiteDatabase.delete("events", "event_id=?", new String[]{String.valueOf(eventID)});
        sqLiteDatabase.close();
    }

    public void updateEvent(UpcomeEventDatabase db, int eventID, UpcomeEvent event, int parent){
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
        sqLiteDatabase.update("events",values, "event_id=?", new String[]{String.valueOf(eventID)});
        sqLiteDatabase.close();
    }

}
