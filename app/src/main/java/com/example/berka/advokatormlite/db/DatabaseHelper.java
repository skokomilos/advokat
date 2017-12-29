package com.example.berka.advokatormlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.CompoundButton;

import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakTarifaJoin;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.Tarifa;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{


    private static final String DATABASE_NAME    = "advokatDatabase";
    private static String DB_PATH = "/data/data/com.example.berka.advokatormlite/databases/";

    Context mContext;

    private static final int DATABASE_VERSION = 1;

    private Dao<Slucaj, Integer> mSlucajDao = null;
    private Dao<Postupak, Integer> mPostupakDao = null;
    private Dao<Tarifa, Integer> mTarifaDao = null;
    private Dao<Radnja, Integer> mRadnjaDao = null;
    private Dao<TabelaBodova, Integer> mTabelaBodovaDao = null;
    private Dao<IzracunatTrosakRadnje, Integer> mIzracunatTrosakRadnjeDao = null;
    private Dao<VrsteParnica, Integer> mVrsteParnicaDao = null;
    private Dao<StrankaDetail, Integer> mStrankaDetail = null;

    private Dao<PostupakVrstaParniceJoin, Integer> mPostupakVrsteParnicaDao = null;
    private Dao<PostupakTarifaJoin, Integer> mPostupakTarifaDao = null;



    public DatabaseHelper(Context context)
    {

        super(context, DB_PATH+DATABASE_NAME, null, DATABASE_VERSION);

        this.mContext = context;

        boolean dbExist = checkdatabase();
        if(!dbExist){

            try {

                File dir = new File(DB_PATH);
                dir.mkdirs();
                InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
                String outfilename = DB_PATH + DATABASE_NAME;
                Log.i(DatabaseHelper.class.getName(), "DB Path : " + outfilename);
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int lenght;
                while ((lenght = myInput.read(buffer)) > 0){
                    myoutput.write(buffer, 0, lenght);
                }

                myoutput.flush();
                myoutput.close();
                myInput.close();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * Check whether or not database exist
     */
    private boolean checkdatabase() {
        boolean checkdb = false;

        String myPath = DB_PATH + DATABASE_NAME;
        File dbfile = new File(myPath);
        checkdb = dbfile.exists();

        Log.i(DatabaseHelper.class.getName(), "DB Exist : " + checkdb);

        return checkdb;
    }


//    @Override
//    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//
//        try
//        {
//            TableUtils.createTable(connectionSource, Postupak.class);
//            TableUtils.createTable(connectionSource, Tarifa.class);
//            TableUtils.createTable(connectionSource, Radnja.class);
//
//        }catch (SQLException e)
//        {
//            throw new RuntimeException(e);
//        }
//
//    }

//    @Override
//    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//
//        try {
//            TableUtils.dropTable(connectionSource, Radnja.class, true);
//            TableUtils.dropTable(connectionSource, Tarifa.class, true);
//            TableUtils.dropTable(connectionSource, Postupak.class, true);
//
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Dao<TabelaBodova, Integer> getmTabelaBodovaDao() throws SQLException{
        if(mTabelaBodovaDao == null){
            mTabelaBodovaDao = getDao(TabelaBodova.class);
        }

        return mTabelaBodovaDao;
    }

    public Dao<IzracunatTrosakRadnje, Integer> getmIzracunatTrosakRadnjeDao() throws SQLException{
        if(mIzracunatTrosakRadnjeDao == null){
            mIzracunatTrosakRadnjeDao = getDao(IzracunatTrosakRadnje.class);
        }

        return mIzracunatTrosakRadnjeDao;
    }


    public Dao<Slucaj, Integer> getSlucajDao() throws SQLException{
        if(mSlucajDao == null) {
            mSlucajDao = getDao(Slucaj.class);
        }

        return mSlucajDao;
    }

    public Dao<Postupak, Integer> getPostupakDao() throws SQLException {
        if (mPostupakDao == null) {
            mPostupakDao = getDao(Postupak.class);
        }

        return mPostupakDao;
    }

    public Dao<Tarifa, Integer> getmTarifaDao() throws SQLException{
        if(mTarifaDao == null){
            mTarifaDao = getDao(Tarifa.class);
        }

        return mTarifaDao;
    }

    public Dao<Radnja, Integer> getmRadnjaDao() throws SQLException{
        if(mRadnjaDao == null){
            mRadnjaDao = getDao(Radnja.class);
        }

        return mRadnjaDao;
    }

    public Dao<VrsteParnica, Integer> getmVrsteParnicaDao() throws SQLException{

        if(mVrsteParnicaDao == null){
            mVrsteParnicaDao = getDao(VrsteParnica.class);
        }

        return mVrsteParnicaDao;
    }

    public Dao<StrankaDetail, Integer> getmStrankaDetail() throws SQLException{

        if(mStrankaDetail == null){
            mStrankaDetail = getDao(StrankaDetail.class);
        }

        return mStrankaDetail;
    }



    public Dao<PostupakVrstaParniceJoin, Integer> getPostupakVrsteParnicaDao() throws SQLException{

        if(mPostupakVrsteParnicaDao == null){
            mPostupakVrsteParnicaDao = getDao(PostupakVrstaParniceJoin.class);
        }

        return mPostupakVrsteParnicaDao;
    }

    public Dao<PostupakTarifaJoin, Integer> getmPostupakTarifaDao() throws SQLException{

        if(mPostupakTarifaDao == null){
            mPostupakTarifaDao = getDao(PostupakTarifaJoin.class);
        }

        return mPostupakTarifaDao;
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        mSlucajDao = null;
        mPostupakDao = null;
        mTarifaDao = null;
        mRadnjaDao = null;
        mTabelaBodovaDao = null;
        mVrsteParnicaDao = null;
        mStrankaDetail = null;
        mPostupakVrsteParnicaDao = null;
        mPostupakTarifaDao = null;

        super.close();
    }
}

