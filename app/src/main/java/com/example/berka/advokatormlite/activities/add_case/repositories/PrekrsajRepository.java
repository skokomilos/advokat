package com.example.berka.advokatormlite.activities.add_case.repositories;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.PrekrsajRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

import javax.inject.Inject;

/**
 * Created by berka on 07-Jun-18.
 */

public class PrekrsajRepository implements PrekrsajRepositoryInterface {

    @Inject
    DatabaseHelper databaseHelper;

    public PrekrsajRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
}
