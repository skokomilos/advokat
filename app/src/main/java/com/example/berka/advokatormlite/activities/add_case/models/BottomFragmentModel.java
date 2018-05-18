package com.example.berka.advokatormlite.activities.add_case.models;

import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.mvp_contracts.BottomFragmentContract;
import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

/**
 * Created by berka on 25-Apr-18.
 */

public class BottomFragmentModel implements BottomFragmentContract.Model{

    private static final String TAG = "BottomFragmentModel";
    private BottomRepositoryInterface repository;

    public BottomFragmentModel(BottomRepositoryInterface repository) {
        this.repository = repository;
    }

    @Override
    public void saveSlucaj(Slucaj slucaj) {
            repository.equals(null);
    }

    @Override
    public void saveStrankaDetails(StrankaDetail strankaDetail) {

        Log.d(TAG, "saveStrankaDetails: dodat je jedan klijentic" + strankaDetail.getIme_i_prezime());
        Log.d(TAG, "saveStrankaDetails: dodat je jedan klijentic" + strankaDetail.getAdresa());
        Log.d(TAG, "saveStrankaDetails: dodat je jedan klijentic" + strankaDetail.getMesto());
    }
}
