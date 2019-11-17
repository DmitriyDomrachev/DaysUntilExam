package com.uraldroid.daystoexam.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SPHelper {
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public static final String COMMUNITY_DATE = "commDate", TAG = "SPHelperTAG";

    public SPHelper(Context context) {
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveValue(String key, String value) {
        editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.d(TAG, "saveValue: " + key);

    }

    public void saveValue(String key, int value) {
        editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
        Log.d(TAG, "saveValue: " + key);

    }

    public void saveValue(String key, long value) {
        editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
        Log.d(TAG, "saveValue: " + key);

    }

    public void saveValue(String key, boolean value) {
        editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
        Log.d(TAG, "saveValue: " + key);

    }

    public int loadValue(String key, int defValue) {
        Log.d(TAG, "loadValue: " + key);

        return sharedPref.getInt(key, defValue);
    }

    public String loadValue(String key, String defValue) {
        Log.d(TAG, "loadValue: " + key);

        return sharedPref.getString(key, defValue);
    }

    public long loadValue(String key, long defValue){
        Log.d(TAG, "loadValue: " + key);

        return  sharedPref.getLong(key, defValue);
    }

    public boolean loadValue(String key, boolean defValue) {
        Log.d(TAG, "loadValue: " + key);

        return sharedPref.getBoolean(key, defValue);
    }



}

