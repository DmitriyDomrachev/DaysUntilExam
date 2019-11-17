package com.uraldroid.daystoexam.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.adapter.LessonsRVAdapter;
import com.uraldroid.daystoexam.data.Lesson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Lesson> lessons = new ArrayList<>();
    RecyclerView rv;
    LessonsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i< 12; i++){
            lessons.add(new Lesson(i));
        }

        rv = findViewById(R.id.main_rv_container);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        adapter = new LessonsRVAdapter(lessons, getApplicationContext());

        rv.setAdapter(adapter);


    }
}
