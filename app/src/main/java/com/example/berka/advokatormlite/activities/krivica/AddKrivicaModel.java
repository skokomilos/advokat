package com.example.berka.advokatormlite.activities.krivica;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 06-Apr-18.
 */

public class AddKrivicaModel implements AddKrivicaContractMVP.Model {

    private AddKrivicaRepository repository;


    public AddKrivicaModel(AddKrivicaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak) {

       return repository.queryForAllZapreceneKazneForKrivica(postupak);
    }

    @Override
    public Slucaj saveSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {
        return repository.insertObjectSlucaj(broj_slucaja, postupak, selectedItem, valueOkrivljenOstecen, broj_stranaka);
    }

    @Override
    public void saveStrankaDetails(StrankaDetail strankaDetail) {

    }
}
