package com.example.berka.advokatormlite.activities.prekrsaj;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.interfaces.ForPresenters;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 25-Feb-18.
 */

public class AddPrekrsajniPresenter extends ForPresenters implements AddPrekrsajniContract.Presenter {

    AddPrekrsajniContract.View view;

    public AddPrekrsajniPresenter(Object view, DatabaseHelper databaseHelper) {
        super(view, databaseHelper);
        this.view = (AddPrekrsajniContract.View) view;
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
        try {
            final List<TabelaBodova> zapreceneKazneList= databaseHelper.getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.POSTUPAK_ID, postupakId)
                    .query();
            Log.d("Imamprob", String.valueOf(zapreceneKazneList.size()));
            view.loadSpinner(zapreceneKazneList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        }catch (NumberFormatException e){
            view.numbersOnlyTestingWarning();
        }
        try {
            databaseHelper.getSlucajDao().create(slucaj);

            ArrayList<StrankaDetail> allClientsDetails = view.allClientsDetailValues();

            for (int i = 0; i < allClientsDetails.size(); i++) {
                allClientsDetails.get(i).setSlucaj(slucaj);
                databaseHelper.getmStrankaDetail().create(allClientsDetails.get(i));
            }
            view.caseAddedMessage(slucaj);
            view.gotoFoundCaseActivity(slucaj.getId());
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        } catch (SQLException e) {
            view.caseWithThisCaseAlreadyExists();
        }
    }

    @Override
    public void saveCaseToDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int okrivljen_ostecen, int brojStranaka) {

    }
}
