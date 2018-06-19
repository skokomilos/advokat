package com.example.berka.advokatormlite.dependencyinjection;

import android.app.Application;

import com.example.berka.advokatormlite.activities.add_case.modules.BottomFragmentModule;
import com.example.berka.advokatormlite.activities.add_case.modules.KrivicaModule;
import com.example.berka.advokatormlite.activities.add_case.modules.ParnicaModule;
import com.example.berka.advokatormlite.activities.add_case.modules.PrekrsajModule;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniModule;
import com.example.berka.advokatormlite.activities.main.DatabaseModule;

/**
 * Created by berka on 05-Mar-18.
 */

public class App extends Application {


    private static App myApp;
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = this;

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .databaseModule(new DatabaseModule())
                .pronadjeniModule(new PronadjeniModule())
                .bottomFragmentModule(new BottomFragmentModule())
                .krivicaModule(new KrivicaModule())
                .parnicaModule(new ParnicaModule())
                .prekrsajModule(new PrekrsajModule())
                .build();
    }

    public static App getMyApp() {
        return myApp;
    }

    public ApplicationComponent getComponent(){
        return component;
    }


}
