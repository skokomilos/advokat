package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Inject;

/**
 * Created by berka on 29-Apr-18.
 */

public class AddSlucajRepository implements AddSlucajRepositoryInterface{

    @Inject
    DatabaseHelper databaseHelper;

    public AddSlucajRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
}
