package com.example.berka.advokatormlite.activities.main;

import android.content.SharedPreferences;

import com.example.berka.advokatormlite.activities.main.MainActivityContractMVP;
import com.example.berka.advokatormlite.activities.main.MainActivityDatabaseHelperRepository;
import com.example.berka.advokatormlite.activities.main.MainActivityModel;
import com.example.berka.advokatormlite.activities.main.MainActivityPresenter;
import com.example.berka.advokatormlite.activities.main.MainActivityRespository;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.data.prefs.PreferencesHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 12-Mar-18.
 */
@Module
public class DatabaseModule {


    @Provides
    public MainActivityContractMVP.Presenter providedMainActivityPresenter(MainActivityContractMVP.Model model){
        return new MainActivityPresenter(model);
    }

    @Provides
    public MainActivityContractMVP.Model providedMainActivityModel(MainActivityRespository repository, PreferencesHelper sharedPreferences){
        return new MainActivityModel(repository, sharedPreferences);
    }

    @Provides
    public MainActivityRespository providedMainActivityRepository(DatabaseHelper databaseHelper){
        return new MainActivityDatabaseHelperRepository(databaseHelper);
    }

}
