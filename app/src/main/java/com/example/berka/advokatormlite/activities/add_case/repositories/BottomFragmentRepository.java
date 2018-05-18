package com.example.berka.advokatormlite.activities.add_case.repositories;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.BottomRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;

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
}
