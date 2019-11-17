package com.uraldroid.daystoexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.activity.LessonActivity;
import com.uraldroid.daystoexam.data.Lesson;

import java.util.ArrayList;

public class LessonsRVAdapter extends RecyclerView.Adapter<LessonsRVAdapter.ContactsViewHolder> {

    private static ArrayList<Lesson> lessons;
    private Context context;
    private final String TAG = "LessonRVAdapter";


    public LessonsRVAdapter(ArrayList<Lesson> lessons, Context context) {
        this.lessons = lessons;
        this.context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, int position) {
        final Lesson lesson = lessons.get(position);
        Log.d(TAG, "onBind: " + lesson.getName());
        holder.txtName.setText(String.valueOf(lesson.getName()));
        holder.txtData.setText("Дней до экзамена: " + lesson.getDays() +"\nДата проведения: " + lesson.getMainDate());
        holder.iconImage.setImageDrawable(lesson.getIcon(context));
        holder.cvListener.setRecord(lesson);

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    //инциализации всех View-элементов
    class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtData;
        ImageView iconImage;
        RelativeLayout rl;


        //Инициализируем слушатели
        CardClickListener cvListener = new CardClickListener();


        ContactsViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_lesson_name);
            txtData = itemView.findViewById(R.id.item_lesson_data);
            iconImage = itemView.findViewById(R.id.item_icon_image_view);
            rl = itemView.findViewById(R.id.lesson_item);

            rl.setOnClickListener(cvListener);


        }

        class CardClickListener implements View.OnClickListener {
            Lesson lesson;

            @Override
            public void onClick(View v) {

            }

            public void setRecord(Lesson lesson) {
                this.lesson = lesson;
            }
        }
    }


}
