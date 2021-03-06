package com.ipk.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcomingActivity extends AppCompatActivity {
    FloatingActionButton fab;
    CheckBox event,meeting, aniversary, birthday;
    RecyclerView eventRcycler;
    ArrayList<UpcomeEvent> upcomeEvents;
    UpcomeEventAdapter eventAdapter;
    TextView currDate, goDate, byDay, byWeek, byMonth;

    Toolbar toolbar;
    private UpcomeEventDatabase db;
    SharedPreferences sharedPreferences;
    String selectedFilter;
    ArrayList<Integer> typeFilterArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences=getSharedPreferences("myPref", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        //setNightMode(getApplicationContext(), sharedPreferences.getBoolean("mode",true));
        setContentView(R.layout.activity_upcoming);

        describe();
        filter();
        selectedFilter="day";
        rycFunction(selectedFilter);

        goDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate=Calendar.getInstance();
                int year= currentDate.get(Calendar.YEAR);
                int month= currentDate.get(Calendar.MONTH);
                int day= currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog= new DatePickerDialog(UpcomingActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        currDate.setText(String.format("%02d/%02d/%04d",dayOfMonth, month+1, year));
                        rycFunction(selectedFilter);

                    }
                }, year, month,day);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
                datePickerDialog.show();
            }
        });



    }

    public void describe(){
        db = new UpcomeEventDatabase(this);
        typeFilterArr= new ArrayList<>();
        //başta hepsi işaretli gelir
        typeFilterArr.add(0);
        typeFilterArr.add(2);
        typeFilterArr.add(3);
        typeFilterArr.add(1);

        toolbar=findViewById(R.id.upcome_toolbar);

        toolbar.setTitle("16011073");
        setSupportActionBar(toolbar);

        event=findViewById(R.id.cb_event);
        meeting=findViewById(R.id.cb_meeting);
        aniversary=findViewById(R.id.cb_aniversary);
        birthday=findViewById(R.id.cb_bday);

        byDay=findViewById(R.id.by_day);

        byDay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        byWeek=findViewById(R.id.by_week);
        byMonth=findViewById(R.id.by_month);

        currDate=findViewById(R.id.current_date);
        goDate=findViewById(R.id.go_date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date current = new Date();
        currDate.setText(sdf.format(current));

        eventRcycler=findViewById(R.id.event_rcy);

        //new meeting button
        fab=findViewById(R.id.upcome_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpcomingActivity.this, EventActivity.class);
                intent.putExtra("eventID", 0);
                startActivity(intent);
            }
        });
    }

    public void filter(){
        byDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter="day";
                byDay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                byWeek.setBackground(getResources().getDrawable((R.drawable.left_border)));
                byMonth.setBackground(getResources().getDrawable((R.drawable.left_border)));
                rycFunction(selectedFilter);

            }
        });

        byWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter="week";
                byDay.setBackgroundColor(getResources().getColor(R.color.white));
                byWeek.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                byMonth.setBackgroundColor(getResources().getColor(R.color.white));
                rycFunction(selectedFilter);
            }
        });

        byMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFilter="month";
                byDay.setBackgroundColor(getResources().getColor(R.color.white));
                byWeek.setBackground(getResources().getDrawable((R.drawable.left_border)));
                byMonth.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                rycFunction(selectedFilter);
            }
        });

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event.isChecked()){
                    //databasede index olarak kaydedildiler. //TODO: db düzeni ://
                    typeFilterArr.add(0);
                }else{
                    typeFilterArr.remove(Integer.valueOf(0));
                }
                rycFunction(selectedFilter);
            }
        });

        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meeting.isChecked()){
                    typeFilterArr.add(1);
                }else{
                    typeFilterArr.remove(Integer.valueOf(1));
                }
                rycFunction(selectedFilter);
            }
        });

        aniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aniversary.isChecked()){
                    typeFilterArr.add(3);
                }else{
                    typeFilterArr.remove(Integer.valueOf(3));
                }
                rycFunction(selectedFilter);
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(birthday.isChecked()){
                    typeFilterArr.add(2);
                }else{
                    typeFilterArr.remove((Integer) 2);
                }
                rycFunction(selectedFilter);
            }
        });
    }

    public void rycFunction(String selectedFilter){
        eventRcycler.setLayoutManager(new LinearLayoutManager(this));
        switch (selectedFilter){
            case "day":
                upcomeEvents=new UpcomeEventDao().getDailyEvents(db, currDate.getText().toString(),typeFilterArr);
                break;
            case "month":
                upcomeEvents=new UpcomeEventDao().getMonthEvents(db, currDate.getText().toString(), typeFilterArr);
                break;
            case "week":
                upcomeEvents=new UpcomeEventDao().getWeekEvents(db, currDate.getText().toString(), typeFilterArr);
                break;
        }
        //upcomeEvents=new UpcomeEventDao().getAllEvents(db);
        //TODO: filtreleyerek add yapılacak : filtrele

        /*UpcomeEvent e1=new UpcomeEvent(1, "etkinlik","ödev","android ödev teslimi", "26/10/2020","10:00", "27/10/2020","11.00","","","");
        UpcomeEvent e2=new UpcomeEvent(2, "toplantı","staj","", "26/10/2020","10:00", "26/10/2020","10:00", "","","");
        UpcomeEvent e3=new UpcomeEvent(3, "dg","ipek dg","", "02/01/2020","10:00", "02/01/2020","10:00","","","");
        UpcomeEvent e4=new UpcomeEvent(4, "yıl dönümü","kader","", "01/09/2020","10:00", "02/01/2020","10:00", "","","");
        UpcomeEvent e5=new UpcomeEvent(5, "etkinlik","ödev","", "26/10/2020","10:00", "02/01/2020","10:00","","","");
        upcomeEvents.add(e1);
        upcomeEvents.add(e2);
        upcomeEvents.add(e3);
        upcomeEvents.add(e4);
        upcomeEvents.add(e5);*/
        //başta db boş
      //  Log.d("takip", "upcomeEvents:" +upcomeEvents.get(0).getLabel());

        eventAdapter = new UpcomeEventAdapter(this, upcomeEvents);
        eventRcycler.setAdapter(eventAdapter);
    }

    public static void setNightMode(Context target , boolean state){

        if (state) {
            //uiManager.enableCarMode(0);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // uiManager.disableCarMode(0);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //kayıttan sonra dönünce direk görüntüleme :))
    @Override
    protected void onResume() {
        super.onResume();
        rycFunction(selectedFilter);
        setNightMode(getApplication(), sharedPreferences.getBoolean("mode", true));
    }

    //settings için
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings){
            Intent intent=new Intent(UpcomingActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
