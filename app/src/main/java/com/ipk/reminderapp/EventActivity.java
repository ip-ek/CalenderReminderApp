package com.ipk.reminderapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.media.RingtoneManager;
import android.net.Uri;
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

    UpcomeEventDatabase db;
    SharedPreferences sharedPreferences;

    int repeatCount=0;

    private final static int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        describe();
        remindEvent();
        updateOrSave();

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
                int id;
                if(0==(id=getIntent().getIntExtra("eventID",0))){
                    addEvent();
                }else{
                    updateEvent();
                }
                //Toast.makeText(getApplicationContext(), "Etkinlik kaydedildi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void addEvent(){
        //TODO: eksik bilgi kontrolü
        UpcomeEvent event= new UpcomeEvent(0,eventType.getSelectedItemPosition(),
                header.getText().toString(),content.getText().toString(),
                startDate.getText().toString(),startTime.getText().toString(),
                endDate.getText().toString(),endTime.getText().toString(),
                "",
                eventFreq.getSelectedItemPosition(),
                eventLoc.getText().toString(),0);
        int id=new UpcomeEventDao().addEvent(db, event, 0);
        Toast.makeText(getApplicationContext(),id+ " numarası ile kaydedildi",Toast.LENGTH_SHORT).show();
        //TODO: if(event==-1)
        //repeatCount
        event.setEventID(id);
        if(eventFreq.getSelectedItemPosition()!=0){
            addRepeatedEvent(event,eventFreq.getSelectedItemPosition());
        }
    }

    public void addRepeatedEvent(UpcomeEvent parentEvent, int type){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar start = Calendar.getInstance();
        Calendar end= Calendar.getInstance();
        try{
            start.setTime(format.parse(parentEvent.getStartDate()));
            end.setTime(format.parse(parentEvent.getEndDate()));
            for(int i=0; i<repeatCount; i++){
                switch (type){
                    case 1:
                        start.add(Calendar.DATE, 1);
                        end.add(Calendar.DATE, 1);
                        //her gün
                        break;
                    case 2:
                        start.add(Calendar.DATE, 7);
                        end.add(Calendar.DATE, 7);
                        //her hafta
                        break;
                    case 3:
                        start.add(Calendar.MONTH,1);
                        end.add(Calendar.MONTH,1);
                        //her ay
                        break;
                    case 4:
                        start.add(Calendar.YEAR, 1);
                        end.add(Calendar.YEAR, 1);
                        //her yıl
                        break;
                }
                UpcomeEvent newEvent = new UpcomeEvent(0, parentEvent.getType(),
                        parentEvent.getLabel(), parentEvent.getContent(),
                        format.format(start.getTime()), parentEvent.getStartTime(),
                        end.toString(), parentEvent.getEndTime(),
                        parentEvent.getRemindTime(),
                        0, parentEvent.getAddress(), parentEvent.getEventID());
                int id=new UpcomeEventDao().addEvent(db, newEvent, parentEvent.getEventID());
                newEvent.setEventID(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateEvent(){
        UpcomeEvent event= new UpcomeEvent(getIntent().getIntExtra("eventID",0),eventType.getSelectedItemPosition(),
                header.getText().toString(),content.getText().toString(),
                startDate.getText().toString(),startTime.getText().toString(),
                endDate.getText().toString(),endTime.getText().toString(),
                "",
                eventFreq.getSelectedItemPosition(),
                eventLoc.getText().toString(), getIntent().getIntExtra("parentEvent",0));
        new UpcomeEventDao().updateEvent(db,getIntent().getIntExtra("eventID",0),event,getIntent().getIntExtra("parentEvent",0));
        Toast.makeText(getApplicationContext(),+ getIntent().getIntExtra("eventID",0)+" numarası ile güncelendi",Toast.LENGTH_SHORT).show();
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
        }else if(requestCode==444){
            if(resultCode==RESULT_OK){
                if(!data.getStringExtra("reminder").equals("Hiçbir zaman")){
                    repeatCount=data.getIntExtra("numberPicker", 1);
                }
                remindTime.setText(data.getStringExtra("reminder"));
                Log.d("takip", "alınan str: "+ data.getStringExtra("reminder"));

            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Tekrar bilgisi alınmadı", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void describe(){

        db = new UpcomeEventDatabase(this);
        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

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
                startActivityForResult(intent, 444);
            }
        });


    }

    public void updateOrSave(){
        int id;
        if(0==(id=getIntent().getIntExtra("eventID",0))){
            //kayda gelmiştir.
            eventSave.setText("Kaydet");
            //bunu string olarak saklayınca spinner sette sıkıntı çıkıyordu.
            //timearr'i hepsine yazıp neden indexof yapmadım ben de bilmiyorum TODO
            switch (sharedPreferences.getInt("timeSpin", 0)){
                case 0:
                    remindTime.setText("Hiçbir zaman");
                    break;
                case 1:
                    remindTime.setText("0 dakika önce");
                    break;
                case 2:
                    remindTime.setText("5 dakika önce");
                    break;
                case 3:
                    remindTime.setText("15 dakika önce");
                    break;
                case 4:
                    remindTime.setText("30 dakika önce");
                    break;
                case 5:
                    remindTime.setText("1 saat önce");
                    break;
                case 6:
                    remindTime.setText("4 saat önce");
                    break;
                case 7:
                    remindTime.setText("1 gün önce");
                    break;
                case 8:
                    remindTime.setText("2 gün önce");
                    break;
                case 9:
                    remindTime.setText("1 hafta önce");
                    break;
            }
            eventFreq.setSelection(sharedPreferences.getInt("freqSpin", 0));
        }else{
            eventSave.setText("Güncelle");
            startDate.setText(getIntent().getStringExtra("startDate"));
            startTime.setText(getIntent().getStringExtra("startTime"));
            endDate.setText(getIntent().getStringExtra("endDate"));
            endTime.setText(getIntent().getStringExtra("endTime"));
            eventLoc.setText(getIntent().getStringExtra("address"));
            header.setText(getIntent().getStringExtra("label"));
            content.setText(getIntent().getStringExtra("content"));
            //eventColor.setText(getIntent().getStringExtra());
            //remindTime.setText(getIntent().getStringExtra());"remindTime"
            //spinner
            eventFreq.setSelection(getIntent().getIntExtra("enventFreq", 0));
            eventType.setSelection(getIntent().getIntExtra("type", 0));
        }
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
