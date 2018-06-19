package com.example.berka.advokatormlite.activities.add_case.models;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.presenters.BottomFragmentPresenter;
import com.example.berka.advokatormlite.activities.add_case.repositories.BottomFragmentRepository;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.sql.SQLException;

/**
 * Created by berka on 25-Apr-18.
 */

public class BottomFragmentModel implements BottomFragmentContract.Model , BottomFragmentContract.RequiredModelOps {

    private static final String TAG = "BottomFragmentModel";
    private BottomRepositoryInterface repository;
    private BottomFragmentPresenter presenter;

    public BottomFragmentModel(BottomRepositoryInterface repository) {
        this.repository = repository;
        this.presenter = new BottomFragmentPresenter(this);
    }

    @Override
    public boolean saveSlucaj(Slucaj slucaj){
        
        boolean isSaved = false;

        Log.d(TAG, "saveSlucaj: " + slucaj.getPostupak());
        try {
          isSaved = repository.insertSlucajToDatabase(slucaj);
        } catch (SQLException e) {
            e.printStackTrace();
        }

       return isSaved;
    }


    @Override
    public void saveStrankaDetails(StrankaDetail strankaDetail) {

        repository.insertStrankaDetailsToDatabase(strankaDetail);
    }

    @Override
    public void sameCaseNumberError(String string) {

       presenter.sameCaseNumberError(string);
    }



}
