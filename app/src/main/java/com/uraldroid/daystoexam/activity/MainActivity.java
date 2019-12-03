package com.uraldroid.daystoexam.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uraldroid.daystoexam.App;
import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.adapter.LessonsRVAdapter;
import com.uraldroid.daystoexam.data.SPHelper;
import com.uraldroid.daystoexam.model.Lesson;
import com.uraldroid.daystoexam.service.NotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.uraldroid.daystoexam.data.SPHelper.CREATED_DB;
import static com.uraldroid.daystoexam.data.SPHelper.DATA_VERISON;
import static com.uraldroid.daystoexam.data.SPHelper.UPDATE_5;

public class MainActivity extends AppCompatActivity {
    ArrayList<Lesson> lessons = new ArrayList<>();
    RecyclerView rv;
    LessonsRVAdapter adapter;
    ImageView settingBtn;
    final String TAG = "mainTag";

    FirebaseDatabase database;
    SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingBtn = findViewById(R.id.settingImageView);


        database = FirebaseDatabase.getInstance();
        spHelper = new SPHelper(getApplicationContext());
        checkDataVersion();

        checkUpdate();

        lessons = new ArrayList<>(App.getInstance().getDatabase().lessonDao().getAll());
        rv = findViewById(R.id.main_rv_container);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new LessonsRVAdapter(lessons, getApplicationContext());

        rv.setAdapter(adapter);

        setUpAlarm();


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void checkUpdate() {
        if (spHelper.loadValue(UPDATE_5, true)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Что нового?")
                    .setMessage("- Добавляйте предметы в избранное, чтобы они всегда были в начале списка." +
                            " \n- Получайте ежедневные напоминания о том, сколько дней осталось до экзамена. " +
                            "В уведомлениях отображаются данные об избранных предметах. " +
                            "\n- Выберите удобное время для получения уведомлений в настройках.")
                    .setPositiveButton("Ок", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            spHelper.saveValue(UPDATE_5, false);
        }
    }

    public void setUpAlarm() {
        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        manager.set(AlarmManager.RTC_WAKEUP,
                (calendar.getTimeInMillis()), pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.updateData();

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void checkDataVersion() {
        Log.d(TAG, "Проверка актуальности данных");
        DatabaseReference reference = database.getReference("version");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Версия таблицы: " + value);

                if (spHelper.loadValue(DATA_VERISON, 0L) < value) {
                    updateData();
                    spHelper.saveValue(DATA_VERISON, value);
                }
                lessons = new ArrayList<>(App.getInstance().getDatabase().lessonDao().getAll());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void updateData() {
        Log.d(TAG, "Загрузка расписания из Database");
        lessons.clear();
        DatabaseReference reference = database.getReference("Lessons");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lesson lesson = snapshot.getValue(Lesson.class);
                    lessons.add(lesson);
                    Log.d(TAG, "Загружен урок: " + lesson.getName());


                }

                if (spHelper.loadValue(CREATED_DB, 0) == 0) {
                    Log.d(TAG, "Сохранение уроков");
                    for (Lesson i : lessons) {
                        i.setFavorite(0);
                        App.getInstance().getDatabase().lessonDao().insert(i);
                    }
                    spHelper.saveValue(CREATED_DB, 1);
                } else {

                    Log.d(TAG, "Обновление уроков");
                    for (Lesson i : lessons) {
                        i.setFavorite(App.getInstance().getDatabase().lessonDao().getById(i.getId()).getFavorite());
                        App.getInstance().getDatabase().lessonDao().update(i);
                    }
                }
                lessons = new ArrayList<>(App.getInstance().getDatabase().lessonDao().getAll());
                Collections.sort(lessons, Lesson.myComparator);
                adapter.updateData(lessons);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
