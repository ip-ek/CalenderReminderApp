package com.ipk.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);


        //todo: dark light mode seçim form mypref

        describe();
        filter();
        rycFunction();

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
                byDay.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                byWeek.setBackground(getResources().getDrawable((R.drawable.left_border)));
                byMonth.setBackground(getResources().getDrawable((R.drawable.left_border)));

            }
        });

        byWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byDay.setBackgroundColor(getResources().getColor(R.color.white));
                byWeek.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                byMonth.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        byMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byDay.setBackgroundColor(getResources().getColor(R.color.white));
                byWeek.setBackground(getResources().getDrawable((R.drawable.left_border)));
                byMonth.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    public void rycFunction(){
        eventRcycler.setLayoutManager(new LinearLayoutManager(this));

        upcomeEvents=new UpcomeEventDao().getAllEvents(db);
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

    //kayıttan sonra dönünce direk görüntüleme :))
    @Override
    protected void onResume() {
        super.onResume();
        rycFunction();
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
