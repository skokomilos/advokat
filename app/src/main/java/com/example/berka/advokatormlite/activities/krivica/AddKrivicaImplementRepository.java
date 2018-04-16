package com.example.berka.advokatormlite.activities.krivica;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 06-Apr-18.
 */

public class AddKrivicaImplementRepository implements AddKrivicaRepository {

    @Inject
    DatabaseHelper databaseHelper;

    public AddKrivicaImplementRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<TabelaBodova> queryForAllZapreceneKazneForKrivica(Postupak postupak) {

         List<TabelaBodova> zapreceneKazneList = null;

        try {
            zapreceneKazneList = (List<TabelaBodova>) databaseHelper.getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return zapreceneKazneList;
    }

    @Override
    public Slucaj insertObjectSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {

        Slucaj slucaj = new Slucaj();
        slucaj.setBroj_slucaja(Integer.parseInt(broj_slucaja));
        slucaj.setPostupak(postupak);
        slucaj.setTabelaBodova(selectedItem);
        slucaj.setOkrivljen(valueOkrivljenOstecen);
        slucaj.setBroj_stranaka(broj_stranaka);

        try{
            databaseHelper.getSlucajDao().create(slucaj);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slucaj;
    }
}
