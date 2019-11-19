package com.uraldroid.daystoexam.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uraldroid.daystoexam.App;
import com.uraldroid.daystoexam.R;
import com.uraldroid.daystoexam.activity.LessonActivity;
import com.uraldroid.daystoexam.model.Lesson;

import java.util.ArrayList;
import java.util.Collections;

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
        holder.txtData.setText("Дней до экзамена: " + lesson.getDays() + "\nДата проведения: " + lesson.getMainDate());
        holder.iconImage.setImageDrawable(lesson.getIcon(context));
        holder.starIcon.setImageDrawable(lesson.getStarIcon(context));
        holder.cvListener.setRecord(lesson);
        holder.scListener.setRecord(lesson, holder.starIcon);

    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void updateData(ArrayList<Lesson> newData) {
        lessons.clear();
        lessons.addAll(newData);
        notifyDataSetChanged();

    }

    public void updateData() {
        lessons.clear();
        lessons.addAll(App.getInstance().getDatabase().lessonDao().getAll());
        Collections.sort(lessons, Lesson.myComparator);
        notifyDataSetChanged();

    }

    //инциализации всех View-элементов
    class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtData;
        ImageView iconImage;
        ImageView starIcon;
        RelativeLayout rl;


        //Инициализируем слушатели
        CardClickListener cvListener = new CardClickListener();
        StarClickListener scListener = new StarClickListener();


        ContactsViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_lesson_name);
            txtData = itemView.findViewById(R.id.item_lesson_data);
            iconImage = itemView.findViewById(R.id.item_icon_image_view);
            starIcon = itemView.findViewById(R.id.item_fav_btn);
            rl = itemView.findViewById(R.id.lesson_item);

            rl.setOnClickListener(cvListener);
            starIcon.setOnClickListener(scListener);


        }

        class CardClickListener implements View.OnClickListener {
            Lesson lesson;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LessonActivity.class);
                intent.putExtra("id", lesson.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            void setRecord(Lesson lesson) {
                this.lesson = lesson;
            }
        }

        class StarClickListener implements View.OnClickListener {
            Lesson lesson;
            ImageView imageView;

            @Override
            public void onClick(View v) {
                if (lesson.getFavorite() == 0) {
                    lesson.setFavorite(1);
                } else {
                    lesson.setFavorite(0);
                }
                App.getInstance().getDatabase().lessonDao().update(lesson);
                imageView.setImageDrawable(lesson.getStarIcon(context));
                updateData();

            }

            void setRecord(Lesson lesson, ImageView imageView) {
                this.lesson = lesson;
                this.imageView = imageView;
            }
        }
    }
}
