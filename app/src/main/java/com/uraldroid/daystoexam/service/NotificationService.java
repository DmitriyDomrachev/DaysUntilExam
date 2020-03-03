package com.uraldroid.daystoexam.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.uraldroid.daystoexam.App;
import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.activity.MainActivity;
import com.uraldroid.daystoexam.data.SPHelper;
import com.uraldroid.daystoexam.model.Lesson;

import java.util.ArrayList;
import java.util.Calendar;

import static com.uraldroid.daystoexam.data.SPHelper.NOTIF_ENABLED;

public class NotificationService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    private final String TAG = "notifTag", CHANNEL_ID = "Дни до экзамена";
    private String notifTitle = "Скоро экзамены!", notifText = "";
    private SPHelper spHelper;
    private Calendar calendar;
    private boolean notifEnabled;

    public NotificationService() {
        super("NotificationService");
        Log.d(TAG, "NotificationService создан");

    }

    @Override
    public void onCreate() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_timer_notification)
                .setContentTitle("Проверка...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Считаем дни до экзаменов."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(1, builder.build());
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "onHandleIntent");

        spHelper = new SPHelper(getApplicationContext());
        calendar = Calendar.getInstance();

        notifEnabled = spHelper.loadValue(NOTIF_ENABLED, true);
        int hourSend = Integer.valueOf(spHelper.loadValue(SPHelper.HOUR_NOTIFICATION, "10")),
                minSend = Integer.valueOf(spHelper.loadValue(SPHelper.MINUTE_NOTIFICATION, "00"));
        Log.d(TAG, "Загруженное время уведомления: " + hourSend + ":" + minSend);
        if (notifEnabled && calendar.get(Calendar.HOUR_OF_DAY) == hourSend && calendar.get(Calendar.MINUTE) == minSend) {
            Log.d(TAG, "Время уведомления");
            sendnotification();
            setUpAlarm(calendar.getTimeInMillis() + 1000 * 60 * 60 * 24);
        } else {
            Log.d(TAG, "Еще не время уведомления");
            if (calendar.get(Calendar.HOUR_OF_DAY) < hourSend
                    || (calendar.get(Calendar.HOUR_OF_DAY) == hourSend && calendar.get(Calendar.MINUTE) < minSend)) {
                calendar.set(Calendar.HOUR_OF_DAY, hourSend);
                calendar.set(Calendar.MINUTE, minSend);
                calendar.set(Calendar.SECOND, 1);
                setUpAlarm(calendar.getTimeInMillis());
                Log.d(TAG, "Зашлем сегодня");
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, hourSend);
                calendar.set(Calendar.MINUTE, minSend);
                calendar.set(Calendar.SECOND, 1);
                setUpAlarm(calendar.getTimeInMillis() + 1000 * 60 * 60 * 24);
                Log.d(TAG, "Зашлем завтра");
            }
        }

        stopSelf();

    }

    public void setUpAlarm(long time) {
        Log.d(TAG, "Уведомление запланировано в: " + time);

        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,
                time, pendingIntent);
    }

    private void sendnotification() {
        createNotificationChannel();
        getData();
        if (notifText.equals("")){
            notifText = "Добавьте экзамены в избранное, чтобы получать о них информацию.";
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_timer_notification)
                .setContentTitle(notifTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notifText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void getData() {
        ArrayList<Lesson> lessons = new ArrayList<Lesson>(App.getInstance().getDatabase().lessonDao().getFavorite());
        for (Lesson i : lessons) {
            if (!notifText.equals(""))
                notifText += "\n";
            notifText += i.getDays() + " дней до " + i.getName();
        }

    }
}
