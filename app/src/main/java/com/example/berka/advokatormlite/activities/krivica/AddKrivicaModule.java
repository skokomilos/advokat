package com.example.berka.advokatormlite.activities.krivica;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 06-Apr-18.
 */
@Module
public class AddKrivicaModule {

    @Provides
    public AddKrivicaContractMVP.Presenter providedAddKrivicaPresenter(AddKrivicaContractMVP.Model model){
        return new AddKrivicaPresenter(model);
    }

    @Provides
    public AddKrivicaContractMVP.Model providedAddKrivicaModel(AddKrivicaRepository repository){
        return new AddKrivicaModel(repository);
    }

    @Provides
    public AddKrivicaRepository providedAddKrivicaRepository(DatabaseHelper databaseHelper){
        return new AddKrivicaImplementRepository(databaseHelper);
    }
}
