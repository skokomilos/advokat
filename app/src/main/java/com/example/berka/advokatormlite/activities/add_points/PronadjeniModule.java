package com.example.berka.advokatormlite.activities.add_points;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import dagger.Module;
import dagger.Provides;

/**
 * Created by berka on 26-Mar-18.
 */
@Module
public class PronadjeniModule {


    @Provides
    public PronadjeniContractMVP.Presenter providedPronadjeniPresenter(PronadjeniContractMVP.Model model){
        return new PronadjeniPresenter(model);
    }

    @Provides
    public PronadjeniContractMVP.Model providedPronadjeniyModel(PronadjeniRepository repository){
        return new PronadjeniModel(repository);
    }

    @Provides
    public PronadjeniRepository providedPronadjeniRepository(DatabaseHelper databaseHelper){
        return new PronadjeniDatabaseRespository(databaseHelper);
    }
}
