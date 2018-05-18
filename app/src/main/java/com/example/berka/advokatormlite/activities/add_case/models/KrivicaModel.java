package com.example.berka.advokatormlite.activities.add_case.models;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.KrivicaContract;
import com.example.berka.advokatormlite.activities.add_case.repositories.KrivicaRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.KrivicaRepositoryInterface;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class KrivicaModel implements KrivicaContract.Model{

    private KrivicaRepositoryInterface repository;

    public KrivicaModel(KrivicaRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak) {
        return null;
    }

    @Override
    public Slucaj saveSlucaj(Slucaj slucaj) {
        return null;
    }

    @Override
    public void saveStrankaDetails(StrankaDetail strankaDetail) {

    }

    @Override
    public List<TabelaBodova> getZapreceneKazne(Postupak postupak) {

        return repository.queryForAllTabelaBodova(postupak);
    }
}
