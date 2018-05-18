package com.example.berka.advokatormlite.activities.add_case.repositories;

import android.support.design.widget.TabLayout;

import com.example.berka.advokatormlite.activities.add_case.repository_interfaces.ParnicaRepositoryInterface;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 18-Apr-18.
 */

public class ParnicaRepository implements ParnicaRepositoryInterface{

    @Inject
    DatabaseHelper databaseHelper;
    private PreparedQuery<Postupak> postupciForVrsteParnicaQuery = null;
    private PreparedQuery<VrsteParnica> vrsteparnicaForPostupakkQuery = null;

    public ParnicaRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

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

    @Override
    public List<TabelaBodova> queryForNeprocenjenuUnikatnuListu(VrsteParnica vrsteParnica, Postupak postupak) {

        List<TabelaBodova> tabelaBodovaList = null;

        try {
            tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnica.getId())
                    .and()
                    .eq(TabelaBodova.POSTUPAK_ID, postupak.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tabelaBodovaList;
    }

    @Override
    public List<TabelaBodova> queryForNeprocenjenu_Ne_UnikatnuListu(VrsteParnica vrsteParnica) {

        List<TabelaBodova> tabelaBodovaList = null;

        try {
            tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnica.getId())
                    .and()
                    .isNull(TabelaBodova.POSTUPAK_ID)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tabelaBodovaList;
    }

    @Override
    public List<TabelaBodova> get_Procenjenu_Listu(VrsteParnica vrsteParnica) {

        List<TabelaBodova> tabelaBodovaList = null;

        try {
            tabelaBodovaList = databaseHelper.getmTabelaBodovaDao().queryBuilder()
                    .where()
                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnica.getId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tabelaBodovaList;
    }

}
