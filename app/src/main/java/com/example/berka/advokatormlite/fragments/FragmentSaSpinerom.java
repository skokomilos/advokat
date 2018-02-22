package com.example.berka.advokatormlite.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.AddParnicaActivity;
import com.example.berka.advokatormlite.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.PostupakVrstaParniceJoin;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by berka on 19-Jan-18.
 */

public class FragmentSaSpinerom extends Fragment {

    private int ispraveId;

    @BindView(R.id.spinnerVrsteParniceIsprave)
    public Spinner spinerProcenjivoNeprocenjivo;

    @BindView(R.id.spinnerKazneIsprave)
    public Spinner spinerSaZapreceneKazne;

    private Postupak postupakIspraveObj;
    private VrsteParnica vrsteParnicaObj;
    private TabelaBodova tabelaBodovaObj;
    private DatabaseHelper databaseHelper;

    private Unbinder unbinder;

    @BindView(R.id.dodaj_ispravu_sa_spinera)
    public Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ispraveId = 6;

        try {
            postupakIspraveObj = getDatabaseHelper().getPostupakDao().queryForId(ispraveId);
            Log.d("TAG", "onCreate: Radi sve bez problema" + postupakIspraveObj.getNazivpostupka());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.dodaj_ispravu_sa_spinera)
    public void izracunajIspravu(){

        Toast.makeText(getActivity(), "Ovo izgleda radi " + tabelaBodovaObj.getBodovi(), Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sa_spinerom, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        try {
            loadSpinners();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSpinners() throws SQLException {


        final List<VrsteParnica> vrsteParnicas = lookUpPostupciForVrstaParnica(postupakIspraveObj);
        ArrayAdapter<VrsteParnica> adapter = new ArrayAdapter<VrsteParnica>(getContext(), android.R.layout.simple_spinner_item, vrsteParnicas);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerProcenjivoNeprocenjivo.setAdapter(adapter);
        spinerProcenjivoNeprocenjivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vrsteParnicaObj = (VrsteParnica) spinerProcenjivoNeprocenjivo.getSelectedItem();
                Log.d("TAG", "onItemSelected: " + vrsteParnicaObj.getId());
                Log.d("TAG", "onItemSelected: " + postupakIspraveObj.getId());
                List<TabelaBodova> tabelaBodovaList = null;
                
                try {
                    //2 znaci da je neprocenjivo
                    if(vrsteParnicaObj.getId() == 2) {
                        Log.d("TAG", "onItemSelected: " + vrsteParnicaObj.getVrstaParnice());
                            tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                    .where()
                                    .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObj.getId())
                                    .and()
                                    .eq(TabelaBodova.POSTUPAK_ID, postupakIspraveObj.getId())
                                    .query();
                    }
                 else{
                        Log.d("TAG", "onItemSelected: " + vrsteParnicaObj.getVrstaParnice());
                        tabelaBodovaList = getDatabaseHelper().getmTabelaBodovaDao().queryBuilder()
                                .where()
                                .eq(TabelaBodova.VRSTE_PARNICA_ID, vrsteParnicaObj.getId())
                                .query();
                    }

                    ArrayAdapter<TabelaBodova> adapterZaTabeluBodova = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, tabelaBodovaList);
                    adapterZaTabeluBodova.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinerSaZapreceneKazne.setAdapter(adapterZaTabeluBodova);
                    spinerSaZapreceneKazne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            tabelaBodovaObj = (TabelaBodova) spinerSaZapreceneKazne.getSelectedItem();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private PreparedQuery<VrsteParnica> vrsteparnicaForPostupakkQuery = null;

    private List<VrsteParnica> lookUpPostupciForVrstaParnica(Postupak postupak) throws SQLException {
        if(vrsteparnicaForPostupakkQuery == null){
            vrsteparnicaForPostupakkQuery = makeVrsteparnicaForPostupak();
        }
        vrsteparnicaForPostupakkQuery.setArgumentHolderValue(0, postupak);
        return databaseHelper.getmVrsteParnicaDao().query(vrsteparnicaForPostupakkQuery);
    }

    private PreparedQuery<VrsteParnica> makeVrsteparnicaForPostupak() throws SQLException {
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


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getContext());
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}