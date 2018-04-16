package com.example.berka.advokatormlite.dependencyinjection;

import android.app.Application;

import com.example.berka.advokatormlite.activities.add_points.PronadjeniModule;
import com.example.berka.advokatormlite.activities.krivica.AddKrivicaModule;
import com.example.berka.advokatormlite.activities.parnica.AddParnicaModule;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

/**
 * Created by berka on 05-Mar-18.
 */

public class App extends Application {

    private ApplicationComponent component;

//    @Inject
//    DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule())
                .pronadjeniModule(new PronadjeniModule())
                .addKrivicaModule(new AddKrivicaModule())
                .addParnicaModule(new AddParnicaModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }
}
