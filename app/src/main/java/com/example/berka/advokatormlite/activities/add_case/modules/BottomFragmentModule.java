package com.example.berka.advokatormlite.activities.add_case.modules;

import com.example.berka.advokatormlite.activities.add_case.models.BottomFragmentModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.BottomFragmentPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.BottomFragmentRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 25-Apr-18.
 */
@Module
public class BottomFragmentModule {

    @Singleton
    @Provides
    public BottomFragmentContract.Presenter providePresenter(BottomFragmentContract.Model model){
        return new BottomFragmentPresenter(model);
    }

    @Provides
    public BottomFragmentContract.Model provideModel(BottomRepositoryInterface repository){
        return new BottomFragmentModel(repository);
    }

    @Provides
    public BottomRepositoryInterface provideRepository(DatabaseHelper databaseHelper){
        return new BottomFragmentRepository(databaseHelper);
    }
}
