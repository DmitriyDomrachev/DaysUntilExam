package com.uraldroid.daystoexam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uraldroid.daystoexam.App;
import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.model.Lesson;

public class LessonActivity extends AppCompatActivity {

    TextView nameTV, datesTV, timesTV, descTV;
    ImageView backBtn, starBtn;
    Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        nameTV = findViewById(R.id.lessonActNameTV);
        datesTV = findViewById(R.id.lessonActDatesTV);
        timesTV = findViewById(R.id.lessonActTimeTV);
        descTV = findViewById(R.id.lessonActDescTV);
        backBtn = findViewById(R.id.lessonActBackImageView);
        starBtn = findViewById(R.id.lessonStarImageView);



    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        lesson = App.getInstance().getDatabase().lessonDao().getById(intent.getIntExtra("id", 0));

        nameTV.setText(lesson.getName());
        datesTV.setText(lesson.getDates());
        timesTV.setText(lesson.getTimes());
        descTV.setText(lesson.getDescription());
        starBtn.setImageDrawable(lesson.getStarIcon(getApplicationContext()));


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lesson.getFavorite() == 0)
                    lesson.setFavorite(1);
                else
                    lesson.setFavorite(0);

                App.getInstance().getDatabase().lessonDao().update(lesson);
                starBtn.setImageDrawable(lesson.getStarIcon(getApplicationContext()));
            }
        });

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
