package com.uraldroid.daystoexam.data;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.uraldroid.daystoexam.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lesson {
    public static final int COMMUNITY = 8, HISTORY = 6,
            CHEMISTRY = 9, INFORMATICS = 0, LITERATURE_DATE = 1,
            ENGILSH_DATE = 10, MATH_BASE_DATE = 4, MATH_PROFILE_DATE = 5,
            RUSSIAN_DATE = 3, BIOLOGY_DATE = 11, GEOGRAPHY_DATE = 2,
            PHISICS_DATE = 7;

    String mainDate;
    String name;
    int id;

    public Lesson(int key) {
        id = key;
        name = setName(id);
        mainDate = setDate(id);
    }

    private String setDate(int key) {
        switch (key) {
            case COMMUNITY:
                return "08.06.2020";
            case HISTORY:
                return "04.06.2020";
            case CHEMISTRY:
                return "08.06.2020";
            case INFORMATICS:
                return "25.05.2020";
            case LITERATURE_DATE:
                return "25.05.2020";
            case ENGILSH_DATE:
                return "11.06.2020";
            case MATH_BASE_DATE:
                return "01.06.2020";
            case MATH_PROFILE_DATE:
                return "01.06.2020";
            case RUSSIAN_DATE:
                return "28.05.2020";
            case BIOLOGY_DATE:
                return "11.06.2020";
            case GEOGRAPHY_DATE:
                return "25.05.2020";
            case PHISICS_DATE:
                return "04.06.2020";
        }
        return "";
    }

    private String setName(int key) {
        switch (key) {
            case COMMUNITY:
                return "Обществознание";
            case HISTORY:
                return "История";
            case CHEMISTRY:
                return "Химия";
            case INFORMATICS:
                return "Информатика";
            case LITERATURE_DATE:
                return "Литература";
            case ENGILSH_DATE:
                return "Иностранный язык";
            case MATH_BASE_DATE:
                return "Математика база";
            case MATH_PROFILE_DATE:
                return "Математика профиль";
            case RUSSIAN_DATE:
                return "Русский язык";
            case BIOLOGY_DATE:
                return "Биология";
            case GEOGRAPHY_DATE:
                return "География";
            case PHISICS_DATE:
                return "Физика";
        }
        return "";
    }

    public Drawable getIcon(Context context) {
        switch (id) {
            case COMMUNITY:
                return ContextCompat.getDrawable(context, R.drawable.ic_community);
            case HISTORY:
                return ContextCompat.getDrawable(context, R.drawable.ic_history);
            case CHEMISTRY:
                return ContextCompat.getDrawable(context, R.drawable.ic_chemistry);
            case INFORMATICS:
                return ContextCompat.getDrawable(context, R.drawable.ic_informatic);
            case LITERATURE_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_literature);
            case ENGILSH_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_english);
            case MATH_BASE_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_mathematics);
            case MATH_PROFILE_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_mathematics);
            case RUSSIAN_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_russan);
            case BIOLOGY_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_bilogy);
            case GEOGRAPHY_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_geography);
            case PHISICS_DATE:
                return ContextCompat.getDrawable(context, R.drawable.ic_phisics);

        }
        return ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground);
    }

    public String getMainDate() {
        return mainDate;
    }

    public String getName() {
        return name;
    }

    public String getDays() {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Date date;
        try {
            date = sdf.parse(getMainDate());
            cal1.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return String.valueOf((int) ((cal1.getTime().getTime() - cal2.getTime().getTime()) / (1000 * 60 * 60 * 24)) );
    }


}
