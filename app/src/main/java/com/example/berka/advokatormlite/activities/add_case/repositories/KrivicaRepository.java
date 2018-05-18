package com.example.berka.advokatormlite.activities.add_case.repositories;

import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.KrivicaRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 18-Apr-18.
 */

public class KrivicaRepository implements KrivicaRepositoryInterface{

    private static final String TAG = "KrivicaRepository";
    @Inject
    DatabaseHelper databaseHelper;

    public KrivicaRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void insertObjectSlucaj(Slucaj slucaj) {

        try{
            databaseHelper.getSlucajDao().create(slucaj);
            Log.d(TAG, "insertObjectSlucaj: slucaj dodat u bazu" );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<TabelaBodova> queryForAllTabelaBodova(Postupak postupak) {

        ArrayList<TabelaBodova> zapreceneKazne = null;
        QueryBuilder<TabelaBodova, Integer> qb;
        try {
            qb = databaseHelper.getmTabelaBodovaDao().queryBuilder();
            qb.where().eq(TabelaBodova.POSTUPAK_ID, postupak.getId());
            zapreceneKazne = (ArrayList<TabelaBodova>) qb.query();
            Log.d(TAG, "queryForAllTabelaBodova: "  + zapreceneKazne.get(0).getTarifni_uslov());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zapreceneKazne;
    }
}
