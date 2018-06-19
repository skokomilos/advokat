package com.example.berka.advokatormlite.activities.add_case.repositories;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.models.BottomFragmentModel;
import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import javax.inject.Inject;

/**
 * Created by berka on 25-Apr-18.
 */

public class BottomFragmentRepository implements BottomRepositoryInterface {

    private static final String TAG = "BottomFragm";
    @Inject
    DatabaseHelper databaseHelper;

    BottomFragmentModel model;


    public BottomFragmentRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.model = new BottomFragmentModel(this);
    }

    @Override
    public boolean insertSlucajToDatabase(Slucaj slucaj) {

        boolean isSaved = true;

        try {
            QueryBuilder<Slucaj, Integer> qb = databaseHelper.getSlucajDao().queryBuilder();
            qb.where().eq(Slucaj.BROJ_SLUCAJA, slucaj.getBroj_slucaja());
            if (qb.queryForFirst() != null) {
                //todo treba vratiti vrednost do view-a
                isSaved = false;
                return isSaved;

            } else {
                databaseHelper.getSlucajDao().create(slucaj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSaved;
    }

    @Override
    public void insertStrankaDetailsToDatabase(StrankaDetail strankaDetail) {

        try {
            databaseHelper.getmStrankaDetail().create(strankaDetail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


