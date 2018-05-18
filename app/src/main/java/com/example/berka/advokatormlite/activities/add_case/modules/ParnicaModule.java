package com.example.berka.advokatormlite.activities.add_case.modules;

import android.provider.ContactsContract;

import com.example.berka.advokatormlite.activities.add_case.models.ParnicaModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.ParnicaContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.ParnicaPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.ParnicaRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.ParnicaRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 18-Apr-18.
 */
@Module
public class ParnicaModule {

    @Singleton
    @Provides
    public ParnicaContract.Presenter providePresenter(ParnicaContract.Model model){
        return new ParnicaPresenter(model);
    }

    @Provides
    public ParnicaContract.Model provideModeil(ParnicaRepositoryInterface repositoryInterface){
        return new ParnicaModel(repositoryInterface);
    }

    @Provides
    public ParnicaRepositoryInterface provideRepostitory(DatabaseHelper databaseHelper){
        return new ParnicaRepository(databaseHelper);
    }
}
