package com.example.berka.advokatormlite.activities.add_points;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.BaseActivity;
import com.example.berka.advokatormlite.activities.konacni.KonacniTrosakSvihRadnjiActivity;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja;
import com.example.berka.advokatormlite.adapter.MyAdapteAddEditStranke;
import com.example.berka.advokatormlite.adapter.MyAdapterSveStranke;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.Tarifa;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.util.ArrayList;
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
            slucaj = getIntent().getParcelableExtra("moj_slucaj");
        }
        //presenter.loadExpandableListViewData(slucaj);
        //todo OVDE MORA DA SE ODKOMETARISE
        setUpToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_stranaka_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
            case R.id.action_show_stranke:
                presenter.loadAllParties(slucaj);
                return true;
            case R.id.action_edit_stranke:
                presenter.loadAllPartiesForChange(slucaj);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //todo ovo isto odkomentarisati obavjezno
    public void setUpToolbar(){
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        //ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(slucaj.getPostupak().getNazivpostupka());
    }

    @OnClick(R.id.izracunaj_vrednost)
    @Override
    public void goToKonacniRacunButtonClicked(){
        Intent intent = new Intent(PronadjeniSlucajActivity1.this, KonacniTrosakSvihRadnjiActivity.class);
        //intent.putExtra(FROM, "myDataKey");
        intent.putExtra("myDataKey", slucaj);
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
    public void showPartiesInDialog(List<StrankaDetail> sveStrankeSlucaja) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucajActivity1.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_sve_stranke, null);
        mBuilder.setTitle("Sve stranke ovog slucaja: ");
        ListView listView = view.findViewById(R.id.lista_sve_stranke);
        MyAdapterSveStranke myAdapterSveStranke = new MyAdapterSveStranke(PronadjeniSlucajActivity1.this, (ArrayList<StrankaDetail>) sveStrankeSlucaja);
        listView.setAdapter(myAdapterSveStranke);
        mBuilder.setNegativeButton("Izadji", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(view);
        AlertDialog alertdio = mBuilder.create();
        alertdio.show();
    }

    @Override
    public void showEditPartiesDialog(List<StrankaDetail> sveStrankeSlucaja) {

        Log.d(TAG, "showEditPartiesDialog: " + sveStrankeSlucaja.get(0).getIme_i_prezime());
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucajActivity1.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_edit_stranke, null);
        mBuilder.setTitle("Dodaj ili izmeni vec postojece podatke o strankama");
        ListView listView = mView.findViewById(R.id.lista_add_edit_stranke);

        MyAdapteAddEditStranke myAdapteAddEditStranke = new MyAdapteAddEditStranke(PronadjeniSlucajActivity1.this, (ArrayList<StrankaDetail>) sveStrankeSlucaja);
        listView.setAdapter(myAdapteAddEditStranke);
        myAdapteAddEditStranke.notifyDataSetChanged();

        mBuilder.setPositiveButton("Potrvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myAdapteAddEditStranke.notifyDataSetChanged();
                presenter.editParties(sveStrankeSlucaja);
            }
        });
        mBuilder.setNegativeButton("Izadji", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
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
    public void dialogFixValuePlusHours(double cena, String nazivRadnje) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_fix_plus_hours);

        final Button no = dialog.findViewById(R.id.fix_hours_box_btn_no);
        Button btnYes = dialog.findViewById(R.id.fix_hours_box_btn_yes);
        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupFixHours);
        final ScrollableNumberPicker scrollableNumberPicker = dialog.findViewById(R.id.snp_dialog_fix_plus_hours);

        radioGroup.check(R.id.radioButtonYes);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if(checkedId==R.id.radioButtonNo) {
                    scrollableNumberPicker.setEnabled(false);
                }else if(checkedId == R.id.radioButtonYes){
                    scrollableNumberPicker.setVisibility(View.VISIBLE);
                }
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double izracunataCena = 0;
                final int selecteid = radioGroup.getCheckedRadioButtonId();

                switch (selecteid){

                    case R.id.radioButtonYes:
                        izracunataCena = cena + (scrollableNumberPicker.getValue()*50);
                        presenter.addRadnjaButtonClicked(izracunataCena, nazivRadnje, slucaj);
                        break;
                    case R.id.radioButtonNo:
                        izracunataCena = cena / 2 + (scrollableNumberPicker.getValue()*50);
                        presenter.addRadnjaButtonClicked(izracunataCena, nazivRadnje, slucaj);
                        break;
                    default:
                        Toast.makeText(PronadjeniSlucajActivity1.this, "Oznaci da li je radnja odrzana", Toast.LENGTH_LONG).show();
                        //todo resiti problem kad cekiram NE da ne dozvoli koriscenje scrolablenumberpickera
                }
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
                dialog.show();

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
        Log.d(TAG, "onResume: " + presenter.toString());
         presenter.loadExpandableListViewData(slucaj);
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
