package com.example.berka.advokatormlite.activities.parnica;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 16-Apr-18.
 */
@Module
public class AddParnicaModule {

    @Provides
    public AddParnicaContractMVP.Presenter providedParnicaPresenter(AddParnicaContractMVP.Model model){
        return new AddParnicaPresenter(model);
    }

    @Provides
    public AddParnicaContractMVP.Model providedParnicaModel(AddParnicaRepository repository){
        return new AddParnicaModel(repository);
    }

    @Provides
    public AddParnicaRepository providedParnicaRepository(DatabaseHelper databaseHelper){
        return new AddParnicaRepositoryImplemented(databaseHelper);
    }
}
