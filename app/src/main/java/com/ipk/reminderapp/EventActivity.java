package com.ipk.reminderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventActivity extends AppCompatActivity {
    TextView startDate, startTime, endDate, endTime, remindTime, eventLoc;
    Spinner eventFreq, eventType;
    EditText header, content;
    ImageView eventColor, locDelete, eventLocImg;
    Button eventSave;

    //settings activity'ninkini kullansak onCreate olmazsa sorun
    ArrayList<String> typeArr = new ArrayList<String>();
    ArrayList<String> freqArr = new ArrayList<>();
    ArrayAdapter freqAdapter;
    ArrayAdapter typeAdapter;

    private final static int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        describe();
        remindEvent();

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate=Calendar.getInstance();
                int year= currentDate.get(Calendar.YEAR);
                int month= currentDate.get(Calendar.MONTH);
                int day= currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog= new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(String.format("%02d/%02d/%04d",dayOfMonth, month+1, year));
                        endDate.setText(String.format("%02d/%02d/%04d",dayOfMonth, month+1, year));
                        DateFormat sdf = new SimpleDateFormat("hh:mm");
                        Calendar now = Calendar.getInstance();
                        Date time = now.getTime();
                        startTime.setText(sdf.format(time));
                        now.add(Calendar.MINUTE, 30);
                        Date newTime = now.getTime();
                        endTime.setText(sdf.format(newTime));

                    }
                }, year, month,day);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
                datePickerDialog.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime =Calendar.getInstance();
                int hourOfDay= currentTime.get(Calendar.HOUR_OF_DAY);
                int minute= currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog=new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                        if(hourOfDay==23){
                            endTime.setText(String.format("%02d:%02d",0,minute));
                            //endDate.setText(startDate.getText().charAt(1)=);
                        }else{
                            endTime.setText(String.format("%02d:%02d",(hourOfDay+1),minute));
                        }

                    }
                },hourOfDay,minute,true);
                timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", timePickerDialog);
                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Ayarla", timePickerDialog);
                timePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Calendar currentDate=Calendar.getInstance();
               int year= currentDate.get(Calendar.YEAR);
               int month= currentDate.get(Calendar.MONTH);
               int day= currentDate.get(Calendar.DAY_OF_MONTH);


               DatePickerDialog datePickerDialog;
               datePickerDialog= new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener(){
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                       endDate.setText(String.format("%02d/%02d/%04d",dayOfMonth, month+1, year));    //04d = 4 basamaklı
                   }
               }, year, month,day);

               SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
               Calendar c = Calendar.getInstance();
               try{
                   c.setTime(sdf.parse(startDate.getText().toString()));
                   Log.d("takip", ""+c);
                   Log.d("takip", startDate.getText().toString());
               }catch (ParseException e) {
                   e.printStackTrace();
               }
               datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

               datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePickerDialog);
               datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
               datePickerDialog.show();
           }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime =Calendar.getInstance();
                final int hour= currentTime.get(Calendar.HOUR_OF_DAY);
                int minute= currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog=new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.setText(String.format("%02d:%02d", hour,minute));
                    }
                },hour,minute,true);
                timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", timePickerDialog);
                timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Ayarla", timePickerDialog);
                timePickerDialog.show();
            }
        });

        locDelete.setVisibility(View.INVISIBLE); //başlangıçta görünmez

        locDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //visibilitysi ayarlanacak. visible değilken basılmaz
                eventLoc.setText("");
                locDelete.setVisibility(View.INVISIBLE);
            }
        });

        if(isServicesOkey()){
            eventLocImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(EventActivity.this,MapActivity.class);
                    startActivityForResult(intent, 123);
                }
            });
        }



        eventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventFreq.getSelectedItem().toString();
                eventType.getSelectedItem().toString();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==123){
            if(resultCode==RESULT_OK){
                eventLoc.setText(data.getStringExtra("adr"));
                Log.d("takip", eventLoc.getText().toString());
                locDelete.setVisibility(View.VISIBLE);
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"Konum Alınamadı", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void describe(){
        startDate=findViewById(R.id.event_start_date);
        startTime=findViewById(R.id.event_start_time);
        endDate=findViewById(R.id.event_end_date);
        endTime=findViewById(R.id.event_end_time);
        remindTime=findViewById(R.id.event_remind_time);
        eventLoc=findViewById(R.id.event_loc);
        header=findViewById(R.id.event_header);
        content=findViewById(R.id.event_content);
        eventColor=findViewById(R.id.event_color);
        locDelete=findViewById(R.id.loc_delete_img);
        eventLocImg=findViewById(R.id.event_loc_img);
        eventSave=findViewById(R.id.event_save);
        eventFreq=findViewById(R.id.event_freq_spin);
        eventType=findViewById(R.id.event_type_spin);

        setFreqArr();
        setTypeArr();

        freqAdapter= new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, freqArr);
        eventFreq.setAdapter(freqAdapter);
        typeAdapter=new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeArr);
        eventType.setAdapter(typeAdapter);


    }

    public void setTimeArr(){
        //strings'ten çekmelisin
        /*timeArr.add("Hiçbir zaman");
        timeArr.add("0 dakika önce");
        timeArr.add("5 dakika önce");
        timeArr.add("15 dakika önce");
        timeArr.add("30 dakika önce");
        timeArr.add("1 saat önce");
        timeArr.add("4 saat önce");
        timeArr.add("1 gün önce");
        timeArr.add("2 gün önce");
        timeArr.add("1 hafta önce");*/
    }
    public void setFreqArr(){
        freqArr.add("Yok");
        freqArr.add("Her Gün");
        freqArr.add("Her Hafta");
        freqArr.add("Her Ay");
        freqArr.add("Her Yıl");
    }
    public void setTypeArr(){
        typeArr.add("Etkinlik");
        typeArr.add("Toplantı");
        typeArr.add("Doğum Günü");
        typeArr.add("Yıldönümü");
    }

    public void remindEvent(){
        remindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EventActivity.this, ReminderActivity.class);
                startActivity(intent);
                /*TODO:
                   yazı yerine switch-case koyabilirsin daha güzel olur*/
            }
        });


    }

    public boolean isServicesOkey(){
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(EventActivity.this);
        if(available== ConnectionResult.SUCCESS){
            //problem yok
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //hata çözülmeli
            Dialog dialog= GoogleApiAvailability.getInstance().getErrorDialog(EventActivity.this, available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "Maps kullanılamıyor", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //bunlar çalışmıyor zaman kalırsa tekrar incele
    public String datePicker(){
        Calendar currentDate=Calendar.getInstance();
        int year= currentDate.get(Calendar.YEAR);
        int month= currentDate.get(Calendar.MONTH);
        int day= currentDate.get(Calendar.DAY_OF_MONTH);
        final String[] date = new String[1];

        DatePickerDialog datePickerDialog;
        datePickerDialog= new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date[0] =dayOfMonth+"/"+month+"/"+year;
            }
        }, year, month,day);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePickerDialog);
        datePickerDialog.show();

        return date[0];
    }

    public String timePicker(){
        Calendar currentTime =Calendar.getInstance();
        final int hour= currentTime.get(Calendar.HOUR_OF_DAY);
        int minute= currentTime.get(Calendar.MINUTE);
        final String[] time = new String[1];

        TimePickerDialog timePickerDialog;
        timePickerDialog=new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time[0]=hour+":"+minute;
            }
        },hour,minute,true);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Ayarla", timePickerDialog);
        timePickerDialog.show();

        return time[0];
    }

}
