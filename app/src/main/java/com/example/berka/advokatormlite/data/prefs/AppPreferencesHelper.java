package com.example.berka.advokatormlite.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by berka on 01-Apr-18.
 */

public class AppPreferencesHelper implements PreferencesHelper{

    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_ADDRESS = "PREF_KEY_CURRENT_USER_ADDRESS";
    private static final String PREF_KEY_CURRENT_USER_PLACE= "PREF_KEY_CURRENT_USER_PLACE";

    private SharedPreferences mPrefs;
    SharedPreferences.Editor editor;


    @Inject
    public AppPreferencesHelper(Context context) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = mPrefs.edit();
    }


    @Override
    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, "");
    }

    @Override
    public void setUserNameAddressPlace(String userName, String address, String place) {
        editor.putString(PREF_KEY_CURRENT_USER_NAME, userName);
        editor.putString(PREF_KEY_CURRENT_USER_ADDRESS, address);
        editor.putString(PREF_KEY_CURRENT_USER_PLACE, place);
        editor.apply();
    }

    @Override
    public String getCurrentAddress() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_ADDRESS, "");
    }

    @Override
    public String getCurrentPlace()
    {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PLACE, "");
    }
}
