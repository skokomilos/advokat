package com.example.berka.advokatormlite.activities.add_points;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.BaseActivity;
import com.example.berka.advokatormlite.activities.konacni.KonacniTrosakSvihRadnjiActivity;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.ExpandableAdapterRadnja1;
import com.example.berka.advokatormlite.adapter.MyAdapteAddEditStranke;
import com.example.berka.advokatormlite.adapter.MyAdapterSveStranke;
import com.example.berka.advokatormlite.dependencyinjection.App;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.Tarifa;

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

public class PronadjeniSlucajActivity1 extends BaseActivity implements PronadjeniContractMVP.View, SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private static final String SLUCAJ_OBJ_STATE = "slucaj_obj_state";
    private static final String TAG = "Pronadjeni";
    private double vrednostJednogBodaUDinarima = 30;
    Toolbar toolbar;

    private SearchView search;
    private ExpandableAdapterRadnja1 noviAdapter;



    @Inject
    PronadjeniContractMVP.Presenter presenter;

    @BindView(R.id.izracunaj_vrednost)
    Button button;

    @BindView(R.id.expandable_listview)
    ExpandableListView expandableListView;

    @BindView(R.id.et_search)
    SearchView search_edit_text;

    private Slucaj slucaj;
    private int lastExpandedPosition = -1;

    /**Napomena!
     *
     *  ExpandableListView is loaded in onResume metod over presenter!
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_listview);

        ((App) getApplication()).getComponent().inject(this);

        ButterKnife.bind(this, PronadjeniSlucajActivity1.this);

        if (savedInstanceState != null) {
            slucaj = savedInstanceState.getParcelable(SLUCAJ_OBJ_STATE);
        } else {
            slucaj = getIntent().getParcelableExtra("moj_slucaj");
        }
        //presenter.loadExpandableListViewData(slucaj);
        //todo OVDE MORA DA SE ODKOMETARISE
        initSearchManager();
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

    public void setUpToolbar(){

        final ActionBar ab = getActionBarToolbar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
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

    // Todo u drugoj metodi displayList() dobijam sve radnje svih postupaka za tu tarifu. Tj kad recimo imam -postupak Parnica = tarifa Podnesak = izlasta mi sve radnje za sve PODNESKE, umesto samo za Podnesak od Parnice
    @Override
    public void populateExpandableListView(List<Tarifa> listViewHeaders, HashMap<Tarifa, List<Radnja>> listHashMap) {

        //todo dodajem novi adapter koji ima filter metodu da isprobam
        noviAdapter = new ExpandableAdapterRadnja1(this, listViewHeaders, listHashMap);
        expandableListView.setAdapter(noviAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Radnja radnja = (Radnja) noviAdapter.getChild(groupPosition, childPosition);
                parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
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


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucajActivity1.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_edit_stranke, null);
        mBuilder.setTitle("Dodaj ili izmeni vec postojece podatke o strankama");
        ListView listView = mView.findViewById(R.id.lista_add_edit_stranke);

        MyAdapteAddEditStranke myAdapteAddEditStranke = new MyAdapteAddEditStranke(PronadjeniSlucajActivity1.this, (ArrayList<StrankaDetail>) sveStrankeSlucaja);
        listView.setAdapter(myAdapteAddEditStranke);
        //myAdapteAddEditStranke.notifyDataSetChanged();

        mBuilder.setPositiveButton("Potrvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                presenter.editParties(sveStrankeSlucaja);
                //myAdapteAddEditStranke.notifyDataSetChanged();
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
        dialog.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
    public void openDialogWithHours(double cena, String nazivRadnje) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucajActivity1.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_with_hours, null);
        EditText editText = mView.findViewById(R.id.dialog_with_hours_edit_text);

        mBuilder.setTitle(nazivRadnje)
                .setMessage("Odaberite broj sati")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int brojSati = Integer.parseInt(editText.getText().toString());
                        final double izracunataCena = cena + (brojSati * 50 * vrednostJednogBodaUDinarima);
                        presenter.addRadnjaButtonClicked(izracunataCena, nazivRadnje, slucaj);
                    }
                })
                .setNegativeButton("Otkazi", null)
                .setView(mView)
                .show();
    }

    @Override
    public void openCancelableCaseDialogWithHours(double cena, String nazivRadnje) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PronadjeniSlucajActivity1.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_with_hours, null);
        EditText editText = mView.findViewById(R.id.dialog_with_hours_edit_text);


        String[] lista = new String[]{"odrzan", "nije odrzan"};

        mBuilder.setTitle(nazivRadnje)
                .setSingleChoiceItems(lista,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which > 0){
                            Toast.makeText(PronadjeniSlucajActivity1.this,"Vrednost je " + which, Toast.LENGTH_SHORT).show();
                            editText.setVisibility(View.GONE);
                        }else {
                            editText.setVisibility(View.VISIBLE);
                        }
                    }
                }).setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog)dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                if(checkedItem.equals("odrzan")){
                    int brojSati = Integer.parseInt(editText.getText().toString());
                    final double izracunataCena = cena + (brojSati * 50 * vrednostJednogBodaUDinarima);
                    presenter.addRadnjaButtonClicked(izracunataCena, nazivRadnje, slucaj);
                }else if(checkedItem.equals("nije odrzan")){
                    int brojSati = Integer.parseInt(editText.getText().toString());
                    //todo sta znaci ovde jedan sat
                    final double izracunataCena = cena / 2 + (brojSati*50* vrednostJednogBodaUDinarima);
                    presenter.addRadnjaButtonClicked(izracunataCena, nazivRadnje, slucaj);
                }
            }
        }).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setView(mView);
        AlertDialog dialog = mBuilder.create();
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
         presenter.loadExpandableListViewData(slucaj);
    }

    public void initSearchManager(){

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.et_search);
        search.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
    }

    private void expandAll(){

        int count = noviAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            
            expandableListView.expandGroup(i);
        }
    }
    private void collapseAll(){
        int count = noviAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }

    @Override
    public boolean onClose() {

        noviAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        noviAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        noviAdapter.filterData(newText);
        if(!newText.equals("")){
            expandAll();
        }else  if (newText.equals("")){
            collapseAll();
        }
        return false;
    }
}
