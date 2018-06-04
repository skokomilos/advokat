package com.example.berka.advokatormlite.activities.add_case.repositories;

import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.sql.SQLException;

import javax.inject.Inject;

/**
 * Created by berka on 25-Apr-18.
 */

public class BottomFragmentRepository implements BottomRepositoryInterface{

    @Inject
    DatabaseHelper databaseHelper;

    public BottomFragmentRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void insertSlucajToDatabase(Slucaj slucaj) {

        Log.d("tag", "  Finalna Finalnda destinacija " + slucaj.getPostupak() + " " + slucaj.getBroj_slucaja());

        //todo ovo posle odkomentarisati, da nebi bezveeze ubacivalo u bazu podataka
//        try {
//            databaseHelper.getSlucajDao().create(slucaj);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void insertStrankaDetailsToDatabase(StrankaDetail strankaDetail) {

        Log.d("tag", "  Finalna Finalnda degeneracija " + strankaDetail.getIme_i_prezime());

//        try {
//            databaseHelper.getmStrankaDetail().create(strankaDetail);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
