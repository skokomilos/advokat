package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 06-Apr-18.
 */
@Module
public class AddKrivicaModule {

    @Provides
    public AddSlucajMVP.Presenter providedAddKrivicaPresenter(AddSlucajMVP.Model model){
        return new AddSlucajPresenter(model);
    }

//    @Provides
//    public AddSlucajMVP.Model providedAddKrivicaModel(AddKrivicaRepository repository){
//        return new AddSlucajModel(repository);
//    }

    @Provides
    public AddKrivicaRepository providedAddKrivicaRepository(DatabaseHelper databaseHelper){
        return new AddKrivicaImplementRepository(databaseHelper);
    }
}
