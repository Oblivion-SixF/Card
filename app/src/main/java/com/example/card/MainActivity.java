package com.example.card;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author MECHREVO
 */
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ToggleButton toggleButton;
    AlarmManager alarmManager;
    PendingIntent pi;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = (ToggleButton) findViewById(R.id.swi);
        toggleButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isChecked()) {

            toggleButton.setBackground(getResources().getDrawable(R.drawable.button_g));
            int[] hour = {8,12,13,17};
            int[] minute = {28,0,43,30};

            final Intent intent = new Intent(this,HintActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            startActivity(intent);

            for (int i = 0; i < 4; i++) {

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

        }else {
            alarmManager.cancel(pi);
            toggleButton.setBackground(getResources().getDrawable(R.drawable.button));
        }
    }
}
