package com.example.berka.advokatormlite.activities.add_case.models;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.UstavniSudContract;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.UstavniSudRepositoryInterface;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public class UstavniSudModel implements UstavniSudContract.Model{

    private UstavniSudRepositoryInterface repository;

    public UstavniSudModel(UstavniSudRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public TabelaBodova getZapreceneKazne(Postupak postupak) {
        return repository.loadZaprecenaKazna(postupak);
    }
}
