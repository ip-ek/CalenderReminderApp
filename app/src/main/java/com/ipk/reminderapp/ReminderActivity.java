package com.ipk.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ReminderActivity extends AppCompatActivity {
    //hatırlatma zamanı için
    CheckBox cNone,c0min,c5min, c15min, c30min, c1hour, c4hour, c1day, c2day, c1week;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

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

        numberPicker=findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        NumberPicker.OnValueChangeListener onValueChangeListener =
                new 	NumberPicker.OnValueChangeListener(){
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        Toast.makeText(ReminderActivity.this,
                                "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                    }
                };

    }



}
