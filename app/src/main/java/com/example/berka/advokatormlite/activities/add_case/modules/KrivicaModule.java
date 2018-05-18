package com.example.berka.advokatormlite.activities.add_case.modules;

import com.example.berka.advokatormlite.activities.add_case.models.KrivicaModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.KrivicaPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.KrivicaRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.KrivicaRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 18-Apr-18.
 */
@Module
public class KrivicaModule {

    @Singleton
    @Provides
    public KrivicaContract.Presenter providesPresenter(KrivicaContract.Model model){
        return new KrivicaPresenter(model);
    }

    @Provides
    public KrivicaContract.Model providesModel(KrivicaRepositoryInterface repository){
        return new KrivicaModel(repository);
    }

    @Provides
    public KrivicaRepositoryInterface providesRepository(DatabaseHelper databaseHelper){
        return new KrivicaRepository(databaseHelper);
    }
}
