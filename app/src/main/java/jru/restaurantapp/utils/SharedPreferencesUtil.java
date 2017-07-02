package jru.restaurantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sen on 3/16/2017.
 */

public class SharedPreferencesUtil {
    private SharedPreferencesUtil() { }

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public SharedPreferencesUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSP() {
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }


    public void putLongValue(String key, long value) {
        mEditor.putLong(key, value).commit();
    }


    public void putIntValue(String key, int value) {
        mEditor.putInt(key, value).commit();
    }


    public void putStringValue(String key, String value) {
        mEditor.putString(key, value).commit();
    }


    public void putBooleanValue(String key, boolean value) {
        mEditor.putBoolean(key, value).commit();
    }


    public long getLongValue(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public int getIntValue(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public String getStringValue(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void clear() {
        mEditor.clear().commit();
    }


    public void remove(String key) {
        mEditor.remove(key).commit();
    }
}
