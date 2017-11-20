package com.example.berka.advokatormlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.adapter.MyAdapterPostupak;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 03-Oct-17.
 */

public class PostupakListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    public static String POSTUPAK_KEY = "POSTUPAK_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postupak_list);

        final ListView listView = (ListView) findViewById(R.id.myListView);

        try {
            List<Postupak> list = getDatabaseHelper().getPostupakDao().queryForAll();

            Log.d("DEBAGING", list.get(1).getNazivpostupka().toString());

            MyAdapterPostupak adapter = new MyAdapterPostupak(PostupakListActivity.this, (ArrayList<Postupak>) list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Postupak postupak = (Postupak) listView.getItemAtPosition(position);

                    Intent intent = new Intent(PostupakListActivity.this, PronadjeniSlucaj.class);
                    intent.putExtra(POSTUPAK_KEY, postupak.getId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
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
