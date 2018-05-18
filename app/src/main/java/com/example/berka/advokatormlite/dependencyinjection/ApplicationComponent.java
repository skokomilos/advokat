package com.example.berka.advokatormlite.dependencyinjection;

import com.example.berka.advokatormlite.activities.add_case.AddSlucaj;
import com.example.berka.advokatormlite.activities.add_case.AddSlucajModule;
import com.example.berka.advokatormlite.activities.add_case.modules.BottomFragmentModule;
import com.example.berka.advokatormlite.activities.add_case.modules.KrivicaModule;
import com.example.berka.advokatormlite.activities.add_case.modules.ParnicaModule;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForParnica;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniModule;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity1;
import com.example.berka.advokatormlite.activities.main.DatabaseModule;
import com.example.berka.advokatormlite.activities.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by berka on 05-Mar-18.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        DatabaseModule.class,
        PronadjeniModule.class,
        AddSlucajModule.class,
        BottomFragmentModule.class,
        KrivicaModule.class,
        ParnicaModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);

    void inject(PronadjeniSlucajActivity1 target);

    void inject(AddSlucaj target);

    void inject(FragmentForDynamicEditText target);

    void inject(UpperFragmentForKrivica target);

    void inject(UpperFragmentForParnica target);
}
