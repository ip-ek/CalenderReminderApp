package com.ipk.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpcomingActivity extends AppCompatActivity {
    FloatingActionButton fab;
    CheckBox event,meeting, aniversary, birthday;
    RecyclerView eventRcycler;
    TextView currDate, goDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        describe();

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
                        currDate.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                }, year, month,day);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
                datePickerDialog.show();
            }
        });



    }

    public void describe(){
        event=findViewById(R.id.cb_event);
        meeting=findViewById(R.id.cb_meeting);
        aniversary=findViewById(R.id.cb_aniversary);
        birthday=findViewById(R.id.cb_bday);

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
                startActivity(intent);
            }
        });
    }
}
