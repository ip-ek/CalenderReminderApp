package com.ipk.reminderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    Spinner timeSpin, freqSpin;
    Button btnSave;
    Switch modeSwitch;
    TextView modeText;

    ArrayList<String> timeArr = new ArrayList<String>();
    ArrayList<String> freqArr = new ArrayList<>();
    ArrayAdapter timeAdapter, freqAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        freqSpin=findViewById(R.id.sett_freq);
        timeSpin=findViewById(R.id.sett_time);
        btnSave=findViewById(R.id.sett_save);
        modeSwitch=findViewById(R.id.sett_switch);
        modeText=findViewById(R.id.sett_mode);

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        modeSwitch.setChecked(sharedPreferences.getBoolean("Mode", false));

        setTimeArr();
        setFreqArr();

        timeAdapter= new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeArr);
        timeSpin.setAdapter(timeAdapter);
        freqAdapter= new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, freqArr);
        freqSpin.setAdapter(freqAdapter);

        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        freqSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modeSwitch.isChecked()){
                    modeText.setText(R.string.sett_dark);
                }else{
                    modeText.setText(R.string.sett_light);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Mode", modeSwitch.isChecked());
                editor.apply();
                editor.commit();
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setTimeArr(){
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
    public void setFreqArr(){
        freqArr.add("");
    }
}
