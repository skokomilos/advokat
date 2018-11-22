package com.example.berka.advokatormlite.activities.parnica;

import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 16-Apr-18.
 */

public class AddParnicaRepositoryImplemented implements AddParnicaRepository {

    @Inject
    DatabaseHelper databaseHelper;

    public AddParnicaRepositoryImplemented(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    //todo odavde
    private PreparedQuery<Postupak> postupciForVrsteParnicaQuery = null;
    @Override
    public List<Postupak> lookUpPostupciForVrstaParnica(VrsteParnica vrsteParnica) throws SQLException {
        if(postupciForVrsteParnicaQuery == null){
            postupciForVrsteParnicaQuery = makePostupciForVrsteParnica();
        }
        postupciForVrsteParnicaQuery.setArgumentHolderValue(0, vrsteParnica);
        return databaseHelper.getPostupakDao().query(postupciForVrsteParnicaQuery);
    }

    @Override
    public PreparedQuery<Postupak> makePostupciForVrsteParnica() throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<PostupakVrstaParniceJoin, Integer> userPostQb = databaseHelper.getPostupakVrsteParnicaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakVrstaParniceJoin.POSTUPAK_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakVrstaParniceJoin.VRSTA_PARNICE_ID, userSelectArg);

        // build our outer query for Post objects
        QueryBuilder<Postupak, Integer> postQb = databaseHelper.getPostupakDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(Postupak.POSTUPAK_ID, userPostQb);
        return postQb.prepare();
    }


    private PreparedQuery<VrsteParnica> vrsteparnicaForPostupakkQuery = null;
    @Override
    public List<VrsteParnica> lookUpPostupciForVrstaParnica(Postupak postupak) throws SQLException {
        if(vrsteparnicaForPostupakkQuery == null){
            vrsteparnicaForPostupakkQuery = makeVrsteparnicaForPostupak();
        }
        vrsteparnicaForPostupakkQuery.setArgumentHolderValue(0, postupak);
        return databaseHelper.getmVrsteParnicaDao().query(vrsteparnicaForPostupakkQuery);
    }

    @Override
    public PreparedQuery<VrsteParnica> makeVrsteparnicaForPostupak() throws SQLException {
        // build our inner query for UserPost objects
        QueryBuilder<PostupakVrstaParniceJoin, Integer> userPostQb = databaseHelper.getPostupakVrsteParnicaDao().queryBuilder();
        // just select the post-id field
        userPostQb.selectColumns(PostupakVrstaParniceJoin.VRSTA_PARNICE_ID);
        SelectArg userSelectArg = new SelectArg();
        // you could also just pass in user1 here
        userPostQb.where().eq(PostupakVrstaParniceJoin.POSTUPAK_ID, userSelectArg);
        // build our outer query for Post objects
        QueryBuilder<VrsteParnica, Integer> postQb = databaseHelper.getmVrsteParnicaDao().queryBuilder();
        // where the id matches in the post-id from the inner query
        postQb.where().in(VrsteParnica.ID, userPostQb);
        return postQb.prepare();
    }
}
