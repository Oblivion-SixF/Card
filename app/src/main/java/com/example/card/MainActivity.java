package com.example.card;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bigkoo.pickerview.TimePickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MECHREVO
 */
public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener
{

    ToggleButton toggleButton;
    EditText text1, text2, text3, text4;
    AlarmManager alarmManager;
    PendingIntent pi;
    DateFormat sdf = new SimpleDateFormat("HH:mm");

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        toggleButton = (ToggleButton) findViewById(R.id.swi);
        text1 = findViewById(R.id.date1);
        text2 = findViewById(R.id.date2);
        text3 = findViewById(R.id.date3);
        text4 = findViewById(R.id.date4);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        toggleButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {

            toggleButton.setBackground(getResources().getDrawable(R.drawable.button_g));
            text1.setFocusable(false);
            text2.setFocusable(false);
            text3.setFocusable(false);
            text4.setFocusable(false);
            text1.setOnClickListener(null);
            text2.setOnClickListener(null);
            text3.setOnClickListener(null);
            text4.setOnClickListener(null);
            int type = 0;
            int[] hour, minute;
            int[] pp = {0, 0, 0, 0};
            if (text1.getText() != null) {
                pp[0] = 1;
                type++;
            }
            if (text2.getText() != null) {
                pp[1] = 1;
                type++;
            }
            if (text3.getText() != null) {
                pp[2] = 1;
                type++;
            }
            if (text4.getText() != null) {
                pp[3] = 1;
                type++;
            }
            hour = new int[type];
            minute = new int[type];
            int yy = 0;
            for (int i = 0; i < 4; i++) {
                if (pp[i] == 1) {
                    if (i + 1 == 1) {
                        String string = text1.getText().toString();
                        String[] split = string.split(":");
                        hour[yy] = Integer.valueOf(split[0]);
                        minute[yy] = Integer.valueOf(split[1]);
                        yy++;
                    }
                    if (i + 1 == 2) {
                        String string = text2.getText().toString();
                        String[] split = string.split(":");
                        hour[yy] = Integer.valueOf(split[0]);
                        minute[yy] = Integer.valueOf(split[1]);
                        yy++;
                    }
                    if (i + 1 == 3) {
                        String string = text3.getText().toString();
                        String[] split = string.split(":");
                        hour[yy] = Integer.valueOf(split[0]);
                        minute[yy] = Integer.valueOf(split[1]);
                        yy++;
                    }
                    if (i + 1 == 4) {
                        String string = text4.getText().toString();
                        String[] split = string.split(":");
                        hour[yy] = Integer.valueOf(split[0]);
                        minute[yy] = Integer.valueOf(split[1]);
                        yy++;
                    }
                }
            }


            final Intent intent = new Intent(this, HintActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    | Intent.FLAG_ACTIVITY_TASK_ON_HOME
            );
//            startActivity(intent);

            for (int i = 0; i < hour.length; i++) {

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour[i]);
                calendar.set(Calendar.MINUTE, minute[i]);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);


                // 当前系统 时间戳
                long systemTime = System.currentTimeMillis();
                Log.d("gatsby", "systemTime->" + System.currentTimeMillis());
                // 闹钟 时间戳
                long alarmTime = calendar.getTimeInMillis();
                Log.d("gatsby", "alarmTime->" + calendar.getTimeInMillis());

                if (systemTime > alarmTime) {
                    // 如果设置的时间小于当前时间,闹钟时间 将添加一天,即明天
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    alarmTime = calendar.getTimeInMillis();
                    Log.d("gatsby", "alarmTime2->" + calendar.getTimeInMillis());
                }

                pi = PendingIntent.getActivity(MainActivity.this, i, intent, 0);
                // 重复每天提醒
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pi);
                Log.d("gatsby", "alarmTime.setRepeating->" + calendar.getTimeInMillis());
            }

        } else {
            alarmManager.cancel(pi);
            toggleButton.setBackground(getResources().getDrawable(R.drawable.button));

            text1.setOnClickListener(this);
            text2.setOnClickListener(this);
            text3.setOnClickListener(this);
            text4.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date1:
                TimePickerView timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        text1.setText(sdf.format(date));
                    }
                }).setType(new boolean[]{false, false, false, true, true, false}).build();
                timePickerView.setDate(Calendar.getInstance());
                timePickerView.show();
                break;
            case R.id.date2:
                TimePickerView timePickerView1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        text2.setText(sdf.format(date));
                    }
                }).setType(new boolean[]{false, false, false, true, true, false}).build();
                timePickerView1.setDate(Calendar.getInstance());
                timePickerView1.show();
                break;
            case R.id.date3:
                TimePickerView timePickerView2 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        text3.setText(sdf.format(date));
                    }
                }).setType(new boolean[]{false, false, false, true, true, false}).build();
                timePickerView2.setDate(Calendar.getInstance());
                timePickerView2.show();
                break;
            default:
                TimePickerView timePickerView3 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        text4.setText(sdf.format(date));
                    }
                }).setType(new boolean[]{false, false, false, true, true, false}).build();
                timePickerView3.setDate(Calendar.getInstance());
                timePickerView3.show();
        }

    }

}
