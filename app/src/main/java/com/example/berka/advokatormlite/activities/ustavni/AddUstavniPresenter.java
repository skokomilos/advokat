package com.example.berka.advokatormlite.activities.ustavni;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.interfaces.ForPresenters;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by berka on 26-Feb-18.
 */

public class AddUstavniPresenter extends ForPresenters implements AddUstavniContract.Presenter {

    AddUstavniContract.View view;
    QueryBuilder<TabelaBodova, Integer> qb;

    public AddUstavniPresenter(Object view, DatabaseHelper databaseHelper) {
        super(view, databaseHelper);
        this.view = (AddUstavniContract.View) view;
    }

    @Override
    public Postupak loadPostupak(int postupakId) {
        Postupak postupak = null;
        try {
            postupak = databaseHelper.getPostupakDao().queryForId(postupakId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postupak;
    }

    @Override
    public void loadZapreceneKazne(int postupakId) {

    }

    @Override
    public void saveCasetoDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int brojStranaka) {
        Slucaj slucaj = null;
        try {
            if (sifraSlucaja == null || sifraSlucaja.trim().equals("")) {
                view.emptyCasePasswordWarning();
            } else {
                slucaj.setBroj_slucaja(Integer.valueOf(sifraSlucaja));
                slucaj.setPostupak(postupak);
                slucaj.setTabelaBodova(cenaBodova);
                slucaj.setBroj_stranaka(brojStranaka);
            }
        } catch (NumberFormatException e) {
            view.numbersOnlyTestingWarning();
        }
        try {
            databaseHelper.getSlucajDao().create(slucaj);
            view.gotoFoundCaseActivity(slucaj.getId());

            ArrayList<StrankaDetail> allClientsDetails = view.allClientsDetailValues();

            for (int i = 0; i < allClientsDetails.size(); i++) {
                allClientsDetails.get(i).setSlucaj(slucaj);
                databaseHelper.getmStrankaDetail().create(allClientsDetails.get(i));
                Log.d("ListaDodat", "lista dodata");
            }
            view.caseAddedMessage(slucaj);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            view.caseWithThisCaseAlreadyExists();
        }
    }

    @Override
    public void saveCaseToDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int okrivljen_ostecen, int brojStranaka) {

    }

    @Override
    public TabelaBodova getSpecificObjectFromTabelaBodova(int id) {
        TabelaBodova tabelaBodova = null;
        try {
            tabelaBodova = databaseHelper.getmTabelaBodovaDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "getSpecificObjectFromTabelaBodova: " + tabelaBodova.getId());
        return tabelaBodova;
    }
}
