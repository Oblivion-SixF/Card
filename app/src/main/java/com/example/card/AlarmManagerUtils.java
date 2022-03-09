package com.example.card;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmManagerUtils {

    //闹钟执行任务的时间间隔
    private static final long TIME_INTERVAL = 10 * 1000;
    private Context context;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;

    private Calendar calendar;

    private AlarmManagerUtils(Context aContext) {
        this.context = aContext;
    }

    //singleton
    private static AlarmManagerUtils instance = null;

    public static AlarmManagerUtils getInstance(Context aContext) {
        if (instance == null) {
            synchronized (AlarmManagerUtils.class) {
                if (instance == null) {
                    instance = new AlarmManagerUtils(aContext);
                }
            }
        }
        return instance;
    }

    public void createGetUpAlarmManager() {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyService.class);
        //每隔10秒启动一次服务
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerStartWork() {

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,50);
        calendar.set(Calendar.SECOND,00);

        //版本适配 System.currentTimeMillis()
        // 6.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
        }
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerWorkOnOthers() {
        //高版本重复设置闹钟达到低版本中setRepeating相同效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + TIME_INTERVAL, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + TIME_INTERVAL, pendingIntent);
        }
    }
}