package com.ipk.reminderapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UpcomeEventDatabase extends SQLiteOpenHelper {
    public UpcomeEventDatabase(@Nullable Context context) {
        super(context, "eventsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE events(" +
                "event_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type INTEGER," +
                "label TEXT," +
                "content TEXT," +
                "startDate TEXT," +
                "startTime TEXT,"+
                "endDate TEXT,"+
                "endTime TEXT,"+
                "remindTime TEXT,"+
                "eventFreq INTEGER,"+
                "address TEXT,"+
                "parentEvent INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //bir aksilik olursa tekrar oluşturmak içinmiş
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
}
