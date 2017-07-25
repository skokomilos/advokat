package com.example.berka.advokatormlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Tarifa;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by berka on 7/21/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{


    private static final String DATABASE_NAME    = "advokatDatabase";
    private static String DB_PATH = "/data/data/com.example.berka.advokatormlite/databases/";

    Context mContext;

    private static final int DATABASE_VERSION = 1;

    private Dao<Postupak, Integer> mPostupakDao = null;
    private Dao<Tarifa, Integer> mTarifaDao = null;
    private Dao<Radnja, Integer> mRadnjaDao = null;

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

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        mPostupakDao = null;
        mTarifaDao = null;
        mRadnjaDao = null;

        super.close();
    }
}

