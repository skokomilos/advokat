package com.example.berka.advokatormlite.interfaces;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;

/**
 * Created by berka on 25-Feb-18.
 */

public abstract class ForPresenters<T> {
     public T view;
     public DatabaseHelper databaseHelper;

    public ForPresenters(T view, DatabaseHelper databaseHelper) {
        this.view = view;
        this.databaseHelper = databaseHelper;
    }
}
