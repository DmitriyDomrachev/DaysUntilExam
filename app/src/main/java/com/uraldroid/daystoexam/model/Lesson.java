package com.uraldroid.daystoexam.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.util.StringUtil;

import com.uraldroid.daystoexam.R;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
public class Lesson {
    @PrimaryKey
    int id;

    int favorite;

    String earlyDate, mainDate, lateDate, description, name;

    public Lesson(int id, String earlyDate, String mainDate, String lateDate, String description, String name) {
        this.id = id;
        this.earlyDate = earlyDate;
        this.mainDate = mainDate;
        this.lateDate = lateDate;
        this.description = description;
        this.name = name;
    }

    public Lesson(int id, int favorite, String earlyDate, String mainDate, String lateDate, String description, String name) {
        this.id = id;
        this.favorite = favorite;
        this.earlyDate = earlyDate;
        this.mainDate = mainDate;
        this.lateDate = lateDate;
        this.description = description;
        this.name = name;
    }

    public Lesson() {
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEarlyDate() {
        return earlyDate;
    }

    public void setEarlyDate(String earlyDate) {
        this.earlyDate = earlyDate;
    }

    public String getMainDate() {
        return mainDate;
    }

    public void setMainDate(String mainDate) {
        this.mainDate = mainDate;
    }

    public String getLateDate() {
        return lateDate;
    }

    public void setLateDate(String lateDate) {
        this.lateDate = lateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCutName() {
        return StringUtils.abbreviate(name,10);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        Date date;
        try {
            date = sdf.parse("10:00 " + getMainDate());
            cal1.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return String.valueOf((int) ((cal1.getTime().getTime() - cal2.getTime().getTime()) / (1000 * 60 * 60 * 24)));
    }

    public Drawable getIcon(Context context) {
        switch (id) {
            case 8:
                return ContextCompat.getDrawable(context, R.drawable.ic_community);
            case 4:
                return ContextCompat.getDrawable(context, R.drawable.ic_history);
            case 11:
                return ContextCompat.getDrawable(context, R.drawable.ic_chemistry);
            case 3:
                return ContextCompat.getDrawable(context, R.drawable.ic_informatic);
            case 5:
                return ContextCompat.getDrawable(context, R.drawable.ic_literature);
            case 2:
                return ContextCompat.getDrawable(context, R.drawable.ic_english);
            case 6:
                return ContextCompat.getDrawable(context, R.drawable.ic_mathematics);
            case 7:
                return ContextCompat.getDrawable(context, R.drawable.ic_mathematics);
            case 9:
                return ContextCompat.getDrawable(context, R.drawable.ic_russan);
            case 0:
                return ContextCompat.getDrawable(context, R.drawable.ic_bilogy);
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.ic_geography);
            case 10:
                return ContextCompat.getDrawable(context, R.drawable.ic_phisics);

        }
        return ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground);
    }

    public Drawable getStarIcon(Context context) {
        if (favorite == 0) {
            Log.d("mainTag", "пустая зведза");
            return ContextCompat.getDrawable(context, R.drawable.ic_star_border_black_24dp);
        }
        else {
            Log.d("mainTag", "непустая зведза");

            return ContextCompat.getDrawable(context, R.drawable.ic_star_black_24dp);
        }
    }

    public String getDates() {
        return "Досрочный день сдачи: " + getEarlyDate() +
                "\nОсновной день сдачи: " + getMainDate() +
                "\nРезервный день сдачи: " + getLateDate();
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", earlyDate='" + earlyDate + '\'' +
                ", mainDate='" + mainDate + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    public String getTimes() {

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        Date date;
        try {
            date = sdf.parse("10:00 " + getMainDate());
            cal1.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = cal1.getTime().getTime() - cal2.getTime().getTime();
        int days = (int) (millis / (1000 * 60 * 60 * 24));
        int hours = (int) (millis % (1000 * 60 * 60 * 24) / (1000 * 60 * 60));
        int min = (int) (millis % (1000 * 60 * 60) / (1000 * 60));

        return days + " дней\n" + hours + " часов\n" + min + " минут";
    }

    public static final Comparator<Lesson> myComparator = new Comparator<Lesson>() {
        @Override
        public int compare(Lesson o1, Lesson o2) {
            return o2.getFavorite()-o1.getFavorite();
        }

    };
}
