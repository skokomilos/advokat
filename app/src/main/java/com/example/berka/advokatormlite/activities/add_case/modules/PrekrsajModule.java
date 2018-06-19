package com.example.berka.advokatormlite.activities.add_case.modules;

import com.example.berka.advokatormlite.activities.add_case.models.PrekrsajModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.PrekrsajContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.PrekrsajPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.PrekrsajRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.PrekrsajRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 18-Apr-18.
 */
@Module
public class PrekrsajModule {

    @Singleton
    @Provides
    public PrekrsajContract.Presenter providesPresenter(PrekrsajContract.Model model){
        return new PrekrsajPresenter(model);
    }

    @Provides
    public PrekrsajContract.Model providesModel(PrekrsajRepositoryInterface repository){
        return new PrekrsajModel(repository);
    }

    @Provides
    public PrekrsajRepositoryInterface providesRepository(DatabaseHelper databaseHelper){
        return new PrekrsajRepository(databaseHelper);
    }
}
