package com.example.berka.advokatormlite.activities.add_case;

import android.util.Log;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.StrankaDetail;

import javax.inject.Inject;

/**
 * Created by berka on 29-Apr-18.
 */

public class AddSlucajRepository implements AddSlucajRepositoryInterface{

    private static final String TAG = "AddSlucajRepository";
    @Inject
    DatabaseHelper databaseHelper;

    public AddSlucajRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void insertStrankaDetailToDatabase(StrankaDetail strankaDetail) {

        Log.d(TAG, "insertStrankaDetailToDatabase: Voala dodato" + strankaDetail.getIme_i_prezime());
    }
}
