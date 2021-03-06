package com.example.berka.advokatormlite.activities.add_case.repositories;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.PrekrsajRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<TabelaBodova> queryForAllTabelaBodova(Postupak postupak) {
        ArrayList<TabelaBodova> zapreceneKazne = null;
        QueryBuilder<TabelaBodova, Integer> qb;
        try {
            qb = databaseHelper.getmTabelaBodovaDao().queryBuilder();
            qb.where().eq(TabelaBodova.POSTUPAK_ID, postupak.getId());
            zapreceneKazne = (ArrayList<TabelaBodova>) qb.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zapreceneKazne;
    }
}
