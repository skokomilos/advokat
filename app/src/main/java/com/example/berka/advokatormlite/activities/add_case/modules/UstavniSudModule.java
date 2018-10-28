package com.example.berka.advokatormlite.activities.add_case.modules;

import com.example.berka.advokatormlite.activities.add_case.models.UstavniSudModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.UstavniSudContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.UstavniSudPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.UstavniSudRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.UstavniSudRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 18-Apr-18.
 */
@Module
public class UstavniSudModule {

    @Singleton
    @Provides
    public UstavniSudContract.Presenter providesPresenter(UstavniSudContract.Model model){
        return new UstavniSudPresenter(model);
    }

    @Provides
    public UstavniSudContract.Model providesModel(UstavniSudRepositoryInterface repository){
        return new UstavniSudModel(repository);
    }

    @Provides
    public UstavniSudRepositoryInterface providesRepository(DatabaseHelper databaseHelper){
        return new UstavniSudRepository(databaseHelper);
    }
}
