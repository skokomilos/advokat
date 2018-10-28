package com.example.berka.advokatormlite.activities.add_case.models;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.PrekrsajContract;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.PrekrsajRepositoryInterface;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class PrekrsajModel implements PrekrsajContract.Model{

    private PrekrsajRepositoryInterface repository;

    public PrekrsajModel(PrekrsajRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<TabelaBodova> getZapreceneKazne(Postupak postupak) {
        return repository.queryForAllTabelaBodova(postupak);
    }
}
