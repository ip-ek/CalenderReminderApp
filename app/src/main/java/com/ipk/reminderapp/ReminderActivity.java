package com.ipk.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {
    //hatırlatma zamanı için
    CheckBox cNone,c0min,c5min, c15min, c30min, c1hour, c4hour, c1day, c2day, c1week;
    NumberPicker numberPicker;
    Button btn_save;
    TextView numPickLabel;

    ArrayList<String> timeArr/*=new ArrayList<>()*/;
    ArrayList<CheckBox> checkArr/*=new ArrayList<>()*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

        btn_save=findViewById(R.id.save_remind_btn);
        timeArr=new ArrayList<>();
        checkArr=new ArrayList<>();
        setTimeArr();


        cNone=findViewById(R.id.c_none);
        c0min=findViewById(R.id.c_0min);
        c5min=findViewById(R.id.c_5min);
        c15min=findViewById(R.id.c_15min);
        c30min=findViewById(R.id.c_30min);
        c1hour=findViewById(R.id.c_1hour);
        c4hour=findViewById(R.id.c_4hour);
        c1day=findViewById(R.id.c_1day);
        c2day=findViewById(R.id.c_2day);
        c1week=findViewById(R.id.c_1week);
//tanımlanmadan önce set edersen null
        setCheckArr();

        numPickLabel=findViewById(R.id.number_picker_label);
        numberPicker=findViewById(R.id.number_picker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);

        NumberPicker.OnValueChangeListener onValueChangeListener =
                new 	NumberPicker.OnValueChangeListener(){
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        Toast.makeText(ReminderActivity.this,
                                "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                    }
                };

        cNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cNone.isChecked()){
                    for(int i=1; i<checkArr.size(); i++){
                        checkArr.get(i).setVisibility(View.GONE);
                    }
                }else{
                    for(int i=1; i<checkArr.size(); i++){
                        checkArr.get(i).setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                String str="";
                if(cNone.isChecked()){
                    str=timeArr.get(0);
                }else{
                    for(int i=1; i<timeArr.size(); i++){
                        if(checkArr.get(i).isChecked()){
                            str=str+timeArr.get(i)+",";
                        }
                    }
                }
                //startActiviyforresult ile geldi
                intent.putExtra("reminder", str);
                Log.d("takip", "gonderilen str: "+str);
                intent.putExtra("numberPicker", numberPicker.getValue());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void setTimeArr() {
        //strings'ten çekmelisin
        timeArr.add("Hiçbir zaman");
        timeArr.add("0 dakika önce");
        timeArr.add("5 dakika önce");
        timeArr.add("15 dakika önce");
        timeArr.add("30 dakika önce");
        timeArr.add("1 saat önce");
        timeArr.add("4 saat önce");
        timeArr.add("1 gün önce");
        timeArr.add("2 gün önce");
        timeArr.add("1 hafta önce");
    }

    public void setCheckArr(){
        checkArr.add(cNone);
        checkArr.add(c0min);
        checkArr.add(c5min);
        checkArr.add(c15min);
        checkArr.add(c30min);
        checkArr.add(c1hour);
        checkArr.add(c4hour);
        checkArr.add(c1day);
        checkArr.add(c2day);
        checkArr.add(c1week);

    }



}
