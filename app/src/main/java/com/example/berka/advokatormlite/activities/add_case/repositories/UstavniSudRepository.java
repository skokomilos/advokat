package com.example.berka.advokatormlite.activities.add_case.repositories;

import android.support.design.widget.TabLayout;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.UstavniSudRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 22-Jun-18.
 */

public class UstavniSudRepository implements UstavniSudRepositoryInterface {

    private static final String TAG = "UstavniSudRepository";

    @Inject
    DatabaseHelper databaseHelper;

    public UstavniSudRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }



    //preimenuj u queryfortabelabodova
    @Override
    public TabelaBodova loadZaprecenaKazna(Postupak postupak) {

        ArrayList<TabelaBodova> arrayList = null;

        QueryBuilder<TabelaBodova, Integer> qb;
        try {
            qb = databaseHelper.getmTabelaBodovaDao().queryBuilder();
            qb.where().eq(TabelaBodova.POSTUPAK_ID, postupak.getId());
            arrayList = (ArrayList<TabelaBodova>) qb.query();
            Log.d(TAG, "loadZaprecenaKazna: " + arrayList.get(0).getBodovi());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayList.get(0);
    }
}
