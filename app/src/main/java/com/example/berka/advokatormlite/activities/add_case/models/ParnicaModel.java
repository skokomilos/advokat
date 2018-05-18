package com.example.berka.advokatormlite.activities.add_case.models;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.ParnicaContract;
import com.example.berka.advokatormlite.activities.add_case.repositories.ParnicaRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.ParnicaRepositoryInterface;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class ParnicaModel implements ParnicaContract.Model{

    ParnicaRepositoryInterface repository;

    public ParnicaModel(ParnicaRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<VrsteParnica> loadZapreceneKazne(Postupak postupak) throws SQLException {
        return repository.lookUpPostupciForVrstaParnica(postupak);
    }

    @Override
    public List<TabelaBodova> get_Unikatne_Neprocenjive_Vrednosti_Iz_TabeleBodova(VrsteParnica vrsteParnica, Postupak postupak) {
        return repository.queryForNeprocenjenuUnikatnuListu(vrsteParnica, postupak);
    }

    @Override
    public List<TabelaBodova> get_Neprocenjive_Vrednosti_TabeleBodova_Koje_Nisu_Unikatne(VrsteParnica vrsteParnica) {
        return repository.queryForNeprocenjenu_Ne_UnikatnuListu(vrsteParnica);
    }

    @Override
    public List<TabelaBodova> get_Procenjene_Vrednosti_TabelaBodova(VrsteParnica vrsteParnica) {
        return repository.get_Procenjenu_Listu(vrsteParnica);
    }
}
