package com.example.berka.advokatormlite.dependencyinjection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.data.prefs.AppPreferencesHelper;
import com.example.berka.advokatormlite.data.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 05-Mar-18.
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }

    @Provides
    @Singleton
    PreferencesHelper provideSharedPrefs(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }


//    @Provides
//    String provideDatabaseName() {
//        return "advokatDatabase.db";
//    }
//
//    @Provides
//    Integer provideDatabaseVersion() {
//        return 1;
//    }


}
