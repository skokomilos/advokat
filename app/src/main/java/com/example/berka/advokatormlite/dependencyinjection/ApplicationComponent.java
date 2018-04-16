package com.example.berka.advokatormlite.dependencyinjection;

import com.example.berka.advokatormlite.activities.add_points.PronadjeniModule;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity1;
import com.example.berka.advokatormlite.activities.krivica.AddKrivicaActivity;
import com.example.berka.advokatormlite.activities.krivica.AddKrivicaModule;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.activities.parnica.AddParnicaModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by berka on 05-Mar-18.
 */

@Singleton
@Component(modules = {ApplicationModule.class, DatabaseModule.class, PronadjeniModule.class, AddKrivicaModule.class, AddParnicaModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);

    void inject(PronadjeniSlucajActivity1 target);

    void inject(AddKrivicaActivity target);
}
