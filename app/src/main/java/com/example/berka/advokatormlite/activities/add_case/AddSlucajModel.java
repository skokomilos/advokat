package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 06-Apr-18.
 */

public class AddSlucajModel implements AddSlucajMVP.Model {

    private AddSlucajRepositoryInterface repository;


    public AddSlucajModel(AddSlucajRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak) {

       return null; // repository.queryForAllZapreceneKazneForKrivica(postupak);
    }

    @Override
    public Slucaj saveSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {
        return null; // repository.insertObjectSlucaj(broj_slucaja, postupak, selectedItem, valueOkrivljenOstecen, broj_stranaka);
    }

    @Override
    public void saveStrankaDetails(StrankaDetail strankaDetail) {

    }

}
