package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 29-Apr-18.
 */
@Module
public class AddSlucajModule {

    @Provides
    public AddSlucajMVP.Presenter providesAddSlucajPresenter(AddSlucajMVP.Model model){
        return new AddSlucajPresenter(model);
    }

    @Provides
    public AddSlucajMVP.Model provideAddSlucajModel(AddSlucajRepositoryInterface repository){
        return new AddSlucajModel(repository);
    }

    @Provides
    public AddSlucajRepositoryInterface provideAddSlucajRepository(DatabaseHelper databaseHelper){
        return new AddSlucajRepository(databaseHelper);
    }
}
