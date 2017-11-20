package com.example.berka.advokatormlite.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddKrivicaActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Postupak postupak;
    private int postupak_id;


    // TODO: 19-Oct-17 init widgets, load spinners  




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_krivica);
    }

    private void initKrivica(){

        postupak_id = getIntent().getExtras().getInt(ChoosePostupakActivity.POSTUPAK_KEY);

        try {

            postupak = getDatabaseHelper().getPostupakDao().queryForId(postupak_id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(AddKrivicaActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
