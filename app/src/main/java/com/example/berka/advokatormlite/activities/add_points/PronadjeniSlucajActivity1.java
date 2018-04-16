package com.example.berka.advokatormlite.activities.add_points;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.BaseActivity;
import com.example.berka.advokatormlite.activities.konacni.KonacniTrosakSvihRadnjiActivity;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by berka on 12-Mar-18.
 */

public class PronadjeniSlucajActivity1 extends BaseActivity implements PronadjeniContractMVP.View {


    private static final String SLUCAJ_OBJ_STATE = "slucaj_obj_state";
    private static final String TAG = "Pronadjeni";


    @Inject
    PronadjeniContractMVP.Presenter presenter;

    @BindView(R.id.izracunaj_vrednost)
    Button button;

    @BindView(R.id.lvExpendable)
    ExpandableListView expandableListView;

    ExpandableAdapterRadnja expandableListAdapter;
    private Slucaj slucaj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ((App) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this, PronadjeniSlucajActivity1.this);

        if (savedInstanceState != null) {
            slucaj = savedInstanceState.getParcelable(SLUCAJ_OBJ_STATE);
        } else {
            slucaj = getIntent().getParcelableExtra("myDataKey");
        }
        //presenter.loadExpandableListViewData(slucaj);
        setUpToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
            case R.id.action_show_stranke:
                //presenter.loadAllParties();
                return true;
            case R.id.action_edit_stranke:
                //presenter.dodajIzmeniStranke();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpToolbar(){
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(slucaj.getPostupak().getNazivpostupka());
    }

    @OnClick(R.id.izracunaj_vrednost)
    @Override
    public void goToKonacniRacunButtonClicked(){
        Intent intent = new Intent(PronadjeniSlucajActivity1.this, KonacniTrosakSvihRadnjiActivity.class);
        //intent.putExtra(FROM, "myDataKey");
        //intent.putExtra("myDataKey", slucaj);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SLUCAJ_OBJ_STATE, slucaj);
    }

    @Override
    public void populateExpandableListView(List<Tarifa> listViewHeaders, HashMap<Tarifa, List<Radnja>> listHashMap) {

        expandableListAdapter = new ExpandableAdapterRadnja(this, listViewHeaders, listHashMap);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Radnja radnja = (Radnja) expandableListView.getItemAtPosition(childPosition);
                Radnja radnja = listHashMap.get(listViewHeaders.get(groupPosition)).get(childPosition);
                presenter.radnjaItemHasBeenClicked(radnja, slucaj);
                return true;
            }
        });
    }

    @Override
    public void showSomeError() {
        Toast.makeText(this, "postoji greska", Toast.LENGTH_LONG).show();
    }

    @Override
    public void radnjaAddedMessage() {
        Toast.makeText(this, "Radnja uspesno dodata", Toast.LENGTH_LONG).show();
    }

    @Override
    public void openDialog(double privremenaCena, String naziv_radnje) {
        new AlertDialog.Builder(this)
                .setTitle(naziv_radnje)
                .setMessage("Vrednost ove radnje iznosi: " + String.valueOf(privremenaCena) + " dinara" + "\n\n" + "Da li zelite da je dodate?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    presenter.addRadnjaButtonClicked(privremenaCena, naziv_radnje, slucaj);
                }).create().show();
    }

    @Override
    public void dialogFixValuePlusHours(double privremenaCena) {

    }

    @Override
    public void cantAddRadnjaShowMessage() {
        Toast.makeText(this, "radnja ne moze da se doda!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Izadji")
                .setMessage("Pritiskom na ok izlazite na glavni meni")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent i=new Intent(PronadjeniSlucajActivity1.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

         presenter.setView(this);
         presenter.loadExpandableListViewData(slucaj);
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}