package com.ipk.reminderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    Button btnSave, btnRingTone;
    Switch modeSwitch;
    TextView modeText, ringTone;

    ArrayList<String> timeArr = new ArrayList<String>();
    ArrayList<String> freqArr = new ArrayList<>();
    ArrayAdapter timeAdapter, freqAdapter;

    SharedPreferences sharedPreferences;
    String ringTonePath;
    int freqSpinInt, timeSpinInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        freqSpin = findViewById(R.id.sett_freq);
        timeSpin = findViewById(R.id.sett_time);
        btnSave = findViewById(R.id.sett_save);
        modeSwitch = findViewById(R.id.sett_switch);
        modeText = findViewById(R.id.sett_mode);
        btnRingTone =findViewById(R.id.sett_btn_ringtone);
        ringTone =findViewById(R.id.sett_ringtone);


        setTimeArr();
        setFreqArr();

        timeAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeArr);
        timeSpin.setAdapter(timeAdapter);
        freqAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, freqArr);
        freqSpin.setAdapter(freqAdapter);

        //adapter'ı shared'dan önce getirmelisin

        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringTone.setText(sharedPreferences.getString("ringTone", alarmUri.toString()));
        timeSpin.setSelection(sharedPreferences.getInt("timeSpin", 0));
        freqSpin.setSelection(sharedPreferences.getInt("freqSpin", 0));
        modeSwitch.setChecked(sharedPreferences.getBoolean("mode", false));
        if (modeSwitch.isChecked()) {
            modeText.setText(R.string.sett_dark);
        } else {
            modeText.setText(R.string.sett_light);
        }


        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSpinInt=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        freqSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                freqSpinInt=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRingTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ringtone manager

                final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_NOTIFICATION);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, 999);
            }
        });


        modeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeSwitch.isChecked()) {
                    modeText.setText(R.string.sett_dark);
                } else {
                    modeText.setText(R.string.sett_light);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("mode", modeSwitch.isChecked());
                editor.putString("ringTone", ringTonePath);
                editor.putInt("freqSpin", freqSpinInt);
                editor.putInt("timeSpin", timeSpinInt);
                editor.apply();
                editor.commit();
                Toast.makeText(getApplicationContext(), "Kaydedildi", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    ringTonePath = uri.toString();
                    //bu telefonu değiştirdiği için hata veriyor amannn
                    //RingtoneManager.setActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_NOTIFICATION, uri);
                    Log.d("takip", "Ringtone:" + ringTonePath);
                    ringTone.setText(ringTonePath);
                    //TODO: ringtonePath ekrana alındığı yazılsın
                    /*Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), alarmUri);
                    ringtone.play();  */
                    //bu da defaultunun çalışma hali
                }
            } else {
                Toast.makeText(getApplicationContext(), "Zil sesi alınamadı", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public void setFreqArr() {
        freqArr.add("Yok");
        freqArr.add("Her Gün");
        freqArr.add("Her Hafta");
        freqArr.add("Her Ay");
        freqArr.add("Her Yıl");
    }
}