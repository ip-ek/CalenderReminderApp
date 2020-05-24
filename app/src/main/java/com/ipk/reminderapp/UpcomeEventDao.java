package com.ipk.reminderapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("label")),
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("startDate")),
                    cursor.getString(cursor.getColumnIndex("startTime")),
                    cursor.getString(cursor.getColumnIndex("endDate")),
                    cursor.getString(cursor.getColumnIndex("endTime")),
                    cursor.getString(cursor.getColumnIndex("remindTime")),
                    cursor.getString(cursor.getColumnIndex("eventFreq")),
                    cursor.getString(cursor.getColumnIndex("address")));
            Log.d("takip", "label: "+ cursor.getString(cursor.getColumnIndex("label")));
            eventArrayList.add(event);
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
