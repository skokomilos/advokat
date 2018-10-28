package com.example.berka.advokatormlite.activities.main;

import android.util.Log;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by berka on 06-Mar-18.
 */

public class MainActivityDatabaseHelperRepository implements MainActivityRespository {


    private static final String TAG = "HelperRepository";
    @Inject
    DatabaseHelper databaseHelper;

    public MainActivityDatabaseHelperRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        Log.d(TAG, "MainActivityDatabaseHelperRepository: " + databaseHelper.toString());
    }

    @Override
    public ArrayList<Slucaj> queryForAllCases() {
        ArrayList<Slucaj> slucajevi = null;
        try {
            slucajevi = (ArrayList<Slucaj>) databaseHelper.getSlucajDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slucajevi;
    }

    @Override
    public Slucaj findCaseById(String slucajId) {
            List<Slucaj> slucajevi = new ArrayList<>();
            QueryBuilder<Slucaj, Integer> qb;
            try {
                qb = databaseHelper.getSlucajDao().queryBuilder();
                qb.where().eq(Slucaj.BROJ_SLUCAJA, Integer.parseInt(slucajId));
                if(qb.queryForFirst() == null){
                    Log.d(TAG, "findCaseById: SLUCAJ NE POSTOJI SA OVOM SIFROM");
                    return null;
                }else {
                    slucajevi = qb.query();
                    return slucajevi.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        Log.d(TAG, "findCaseById: da li je ovo ispisuje");
            return null;
    }

    @Override
    public List<Postupak> queryForAllPostupci() {
            List<Postupak> postupciList = null;
        try {
            postupciList = databaseHelper.getPostupakDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postupciList;
    }

}
