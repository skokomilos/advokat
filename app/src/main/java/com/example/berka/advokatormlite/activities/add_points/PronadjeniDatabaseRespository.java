package com.example.berka.advokatormlite.activities.add_points;

import android.util.Log;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakTarifaJoin;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.Tarifa;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 12-Mar-18.
 */

public class PronadjeniDatabaseRespository implements PronadjeniRepository {

    private static final String TAG = "PronadjeniDataba";
    @Inject
    DatabaseHelper databaseHelper;

    public PronadjeniDatabaseRespository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void insertRadnja(double cenaRadnje, String naziv_radnje, Slucaj slucaj) {

        IzracunatTrosakRadnje izracunatTrosakRadnje = new IzracunatTrosakRadnje();
        izracunatTrosakRadnje.setDatum(getNowDate());
        izracunatTrosakRadnje.setCena_izracunate_jedinstvene_radnje(cenaRadnje);
        izracunatTrosakRadnje.setNaziv_radnje(naziv_radnje);
        izracunatTrosakRadnje.setSlucaj(slucaj);

        Log.d("PronadjeniDatabase", "insertRadnja: " + izracunatTrosakRadnje.getDatum());
        Log.d("PronadjeniDatabase", "insertRadnja: " + izracunatTrosakRadnje.getNaziv_radnje());
        Log.d("PronadjeniDatabase", "insertRadnja: " + izracunatTrosakRadnje.getCena_izracunate_jedinstvene_radnje());
        try {
            databaseHelper.getmIzracunatTrosakRadnjeDao().create(izracunatTrosakRadnje);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Tarifa> queryForKrivicaHeaders(Postupak postupak) {
        
        List<Tarifa> headers = null;

        try {
            headers = databaseHelper.getmTarifaDao().queryBuilder()
                    .where()
                    .eq(Tarifa.FIELD_TARIFA_POSTUPAK, postupak.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return headers;
    }


    @Override
    public List<Tarifa> queryForNonKrivicaHeaders(Postupak postupak) {

        List<Tarifa> headers = null;

        try {
            headers = lookUpTarifeForPostupak(postupak);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return headers;
    }


    @Override
    public HashMap<Tarifa, List<Radnja>> queryForNonKrivicaHashMap(List<Tarifa> headers, Postupak postupak) {
        HashMap<Tarifa, List<Radnja>> listHashMap = new HashMap<>();
        List<Radnja> radnje = new ArrayList<>();

        for (int i = 0; i < headers.size(); i++) {
            try {
                radnje = databaseHelper.getmRadnjaDao().queryBuilder()
                        .where()
                        .eq(Radnja.FIELD_RADNJA_TARIFA, headers.get(i).getId())
                        .and()
                        .eq(Radnja.FIELD_SIFRA_POSTUPKA, postupak.getId())
                        .query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "queryForNonKrivicaHashMap: SUSTINA " + headers.get(i).getNaslov_tarife()  + " " + radnje.size());
            listHashMap.put(headers.get(i), radnje);
        }

        return listHashMap;
    }

    @Override
    public List<StrankaDetail> loadForAllPartiesForThisCase(Slucaj slucaj) {

        List<StrankaDetail> lista = null;
        try {
             lista = databaseHelper.getmStrankaDetail().queryBuilder()
                    .where()
                    .eq(StrankaDetail.ID_SLUCAJA, slucaj.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void updateStranka(StrankaDetail strankaDetail) {

        try {
            databaseHelper.getmStrankaDetail().update(strankaDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getNowDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String datestring = sdf.format(c.getTime());
        return datestring;
    }

//TODO ODAVDE METODE VISE NA VISE
    //metode many to many  https://github.com/j256/ormlite-jdbc/tree/master/src/test/java/com/j256/ormlite/examples/manytomany

    private PreparedQuery<Postupak> postupciForTarifaQuery = null;

    private List<Postupak> lookUpPostupciForTarifa(Tarifa tarifa) throws SQLException {
        if(postupciForTarifaQuery == null){
            postupciForTarifaQuery = makePostupciForTarifa();
        }
        postupciForTarifaQuery.setArgumentHolderValue(0, tarifa);
        return databaseHelper.getPostupakDao().query(postupciForTarifaQuery);
    }

    private PreparedQuery<Postupak> makePostupciForTarifa() throws SQLException{

        // build our inner query for UserPost objects
        QueryBuilder<PostupakTarifaJoin, Integer> userPostQb = databaseHelper.getmPostupakTarifaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakTarifaJoin.POSTUPAK_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakTarifaJoin.TARIFA_ID, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<Postupak, Integer> postQb = databaseHelper.getPostupakDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Postupak.POSTUPAK_ID, userPostQb);
        return postQb.prepare();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    private PreparedQuery<Tarifa> tarifeForPostupakQuery = null;

    private List<Tarifa> lookUpTarifeForPostupak(Postupak postupak) throws SQLException{

        if(tarifeForPostupakQuery == null){
            tarifeForPostupakQuery = makeTarifeForPostupak();
        }
        tarifeForPostupakQuery.setArgumentHolderValue(0, postupak);
        return databaseHelper.getmTarifaDao().query(tarifeForPostupakQuery);

    }

    private PreparedQuery<Tarifa> makeTarifeForPostupak() throws SQLException{

        // build our inner query for UserPost objects
        QueryBuilder<PostupakTarifaJoin, Integer> userPostQb = databaseHelper.getmPostupakTarifaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakTarifaJoin.TARIFA_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakTarifaJoin.POSTUPAK_ID, userSelectArg);
        // build our outer query for Post objects
        QueryBuilder<Tarifa, Integer> postQb = databaseHelper.getmTarifaDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Tarifa.FIELD_TARIFA_ID, userPostQb);
        return postQb.prepare();
    }
}
