package com.example.berka.advokatormlite.db;

        import android.content.Context;

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
        import com.j256.ormlite.android.AndroidConnectionSource;
        import com.j256.ormlite.android.DatabaseTableConfigUtil;
        import com.j256.ormlite.dao.Dao;
        import com.j256.ormlite.dao.DaoManager;
        import com.j256.ormlite.table.DatabaseTableConfig;
        import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
        import java.sql.SQLException;

/**
 * Created by berka on 7/21/2017.
 */

public class DatabaseHelper extends SQLiteAssetHelper {

    protected AndroidConnectionSource mConnectionSource = new AndroidConnectionSource(this);

    private static final String DATABASE_NAME = "advokatDatabase.db";

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

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;

    }


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


    private <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(mConnectionSource, clazz);
        if (dao == null) {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(mConnectionSource, clazz);
            if (tableConfig == null) {
                /**
                 * TODO: we have to do this to get to see if they are using the deprecated annotations like
                 * {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(mConnectionSource, clazz);
            } else {
                dao = (Dao<T, ?>) DaoManager.createDao(mConnectionSource, tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
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

