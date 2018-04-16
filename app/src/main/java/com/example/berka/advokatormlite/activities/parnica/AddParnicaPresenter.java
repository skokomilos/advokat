package com.example.berka.advokatormlite.activities.parnica;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.krivica.AddKrivicaContractMVP;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 24-Feb-18.
 */

public class AddParnicaPresenter implements AddParnicaContractMVP.Presenter{

    public static final String TAG = "AddParnicaPresenter";

    @Nullable
    private AddParnicaContractMVP.View view;
    private AddParnicaContractMVP.Model model;

    public AddParnicaPresenter(AddParnicaContractMVP.Model model) {
        this.model = model;
    }


    @Override
    public void addEditTextsProgramacly(int broj_stranaka) {
        for (int i = 0; i < broj_stranaka; i++) {
            view.addredniBrojStranke(i);
            view.addEditTextZaImeStraneke();
            view.addEditTextZaAdresu();
            view.addEditTextZaMesto();
        }
    }

    @Override
    public void setView(AddParnicaContractMVP.View view) {
        this.view = view;
    }

    @Override
    public void loadSpinners(Postupak postupak) {

    }

    @Override
    public int radioButtonCurrentValue(String radiovalue) {
        return 0;
    }

    @Override
    public void saveCaseButtonClicked(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka) {

    }

//    @Override
//    public Postupak loadPostupak(int postupakId){
//        Postupak postupak = null;
//        try {
//            postupak = databaseHelper.getPostupakDao().queryForId(postupakId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return postupak;
//    }
//
//    @Override
//    public void loadZapreceneKazne(int postupakId) {
//
//    }
//
//    @Override
//    public void loadListOkrivljenOstecen(Postupak postupak) {
//        List<VrsteParnica> vrsteParnicaLista = null;
//        try {
//            vrsteParnicaLista = lookUpPostupciForVrstaParnica(postupak);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        //Vrsta parnice - promenljiva ili nepromenljiva
//        view.loadSpinnerPromenljivoNepromenljivo(vrsteParnicaLista);
//    }
//
//    @Override
//    public void loadTabelaBodova(VrsteParnica okrivljen_ostecen, Postupak postupak) {
//
//        List<TabelaBodova> tabelaBodovaList = null;
//        try {
//            //procenjiva vrednost je za sve ista za sve vrste postupaka ali neprocenjiva nije ista za svako, ovih cetri vrednosti(vanparnica, uprava postupci, uprava sporovi, ostali) imaju unikatne vrednosti u DB u tabeli bodova.
//            //broj 2 = neprocenjivo
//            if (okrivljen_ostecen.getId() == 2) {
//                if (postupak.getNazivpostupka().equals("Vanparnicni postupak") || postupak.getNazivpostupka().equals("Upravni postupak") || postupak.getNazivpostupka().equals("Upravni sporovi") || postupak.getNazivpostupka().equals("Ostali postupci")) {
//                    tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
//                            .where()
//                            .eq(TabelaBodova.VRSTE_PARNICA_ID, okrivljen_ostecen.getId())
//                            .and()
//                            .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
//                            .query();
//                } else {
//                    tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
//                            .where()
//                            .eq(TabelaBodova.VRSTE_PARNICA_ID, okrivljen_ostecen.getId())
//                            .and()
//                            .isNull(TabelaBodova.POSTUPAK_ID)
//                            .query();
//                }
//            } else if (okrivljen_ostecen.getId() == 3) {
//                //broj 3 = procenjivo
//                tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
//                        .where()
//                        .eq(TabelaBodova.VRSTE_PARNICA_ID, okrivljen_ostecen.getId())
//                        .query();
//            }
//            view.loadSpinner(tabelaBodovaList);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void saveCasetoDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int brojStranaka) {
//        Slucaj slucaj = null;
//        try {
//            if (sifraSlucaja == null || sifraSlucaja.trim().equals("")) {
//                view.emptyCasePasswordWarning();
//            } else {
//                slucaj.setBroj_slucaja(Integer.valueOf(sifraSlucaja));
//                slucaj.setPostupak(postupak);
//                slucaj.setTabelaBodova(cenaBodova);
//                slucaj.setBroj_stranaka(brojStranaka);
//            }
//        }catch (NumberFormatException e){
//            view.numbersOnlyTestingWarning();
//        }
//        try {
//            databaseHelper.getSlucajDao().create(slucaj);
//            view.gotoFoundCaseActivity(slucaj.getId());
//
//            ArrayList<StrankaDetail> allClientsDetails = view.allClientsDetailValues();
//
//            for (int i = 0; i < allClientsDetails.size(); i++) {
//                allClientsDetails.get(i).setSlucaj(slucaj);
//                databaseHelper.getmStrankaDetail().create(allClientsDetails.get(i));
//                Log.d("ListaDodat", "lista dodata");
//            }
//            view.caseAddedMessage(slucaj);
//        }catch (SQLiteConstraintException e){
//            e.printStackTrace();
//        } catch (SQLException e) {
//            view.caseWithThisCaseAlreadyExists();
//        }
//    }
//
//    @Override
//    public void saveCaseToDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int okrivljen_ostecen, int brojStranaka) {
//
//    }
}
