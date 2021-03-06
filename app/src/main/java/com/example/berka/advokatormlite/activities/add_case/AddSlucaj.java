package com.example.berka.advokatormlite.activities.add_case;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForParnica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentPrekrsaj;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentUstavniSud;
import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;

import butterknife.ButterKnife;

/**
 * Created by berka on 19-Oct-17.
 */

public class AddSlucaj extends AppCompatActivity implements OnAddCaseButtonClicked {

    private static final String TAG = "AddSlucaj";
    private static final String POSTUPAK_STATE = "postupak_state";
    private static final String BROJ_STRANAKA_STATE = "broj_stranaka_state";


    private int redni_broj_prvog_et_u_grid_ll = 2;

    public static final String FROM = "from";
    public static final String CASE_ID = "case_id";

    private Postupak postupak;
    private int broj_stranaka;

    private StrankaDynamicViews strankaDynamicViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_krivica);

        ButterKnife.bind(AddSlucaj.this);
        strankaDynamicViews = new StrankaDynamicViews(this);

        if (findViewById(R.id.upper_fragment_container) != null && findViewById(R.id.dymanic_fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            } else {
                postupak = getIntent().getParcelableExtra("myDataKey");
                broj_stranaka = getIntent().getExtras().getInt("numberOfParties");
            }
        }

//
//        if (savedInstanceState != null) {
//            postupak = savedInstanceState.getParcelable(POSTUPAK_STATE);
//            //broj_stranaka = savedInstanceState.getParcelable(BROJ_STRANAKA_STATE);
//        } else {
//            postupak = getIntent().getParcelableExtra("myDataKey");
//            broj_stranaka = getIntent().getExtras().getInt("numberOfParties");
//        }


        switch (postupak.getNazivpostupka()) {
            case "Krivični postupak":
                UpperFragmentForKrivica upperFragmentForKrivica = new UpperFragmentForKrivica();
                createUpperFragment(new UpperFragmentForKrivica(), postupak, broj_stranaka);
                break;
            case "Prekršajni postupak i postupak privrednih prestupa":
                UpperFragmentPrekrsaj upperFragmentPrekrsaj = new UpperFragmentPrekrsaj();
                createUpperFragment(new UpperFragmentPrekrsaj(), postupak, broj_stranaka);
                break;
            case "Postupak pred ustavnim sudom":
                UpperFragmentUstavniSud upperFragmentUstavniSud = new UpperFragmentUstavniSud();
                createUpperFragment(new UpperFragmentUstavniSud(), postupak, broj_stranaka);
                break;
            default:
                Log.d(TAG, "onCreate: default vrednost");
                UpperFragmentForParnica upperFragmentForParnica = new UpperFragmentForParnica();
                createUpperFragment(new UpperFragmentForParnica(), postupak, broj_stranaka);
                break;
        }

        //down fragment
        Bundle bundle = new Bundle();
        bundle.putInt("brojstranaka", broj_stranaka);

        FragmentForDynamicEditText fragmentForDynamicEditText = new FragmentForDynamicEditText();
        fragmentForDynamicEditText.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.dymanic_fragment_container, fragmentForDynamicEditText)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Izadji")
                .setMessage("Pritiskom na ok izlazite na glavni meni")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    Intent i = new Intent(AddSlucaj.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }).create().show();

    }

    private void createUpperFragment(Fragment fragment, Postupak postupak, int broj_stranaka) {

        Bundle bundle_for_case_obj = new Bundle();
        bundle_for_case_obj.putParcelable("myDataKey", postupak);
        bundle_for_case_obj.putInt("brojstranaka", broj_stranaka);

        fragment.setArguments(bundle_for_case_obj);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.upper_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void setCase(Slucaj slucaj) {

        FragmentForDynamicEditText bottomfrag = (FragmentForDynamicEditText) getSupportFragmentManager().findFragmentById(R.id.dymanic_fragment_container);

        if (bottomfrag != null) {

            bottomfrag.updateFromMain("usao sam u setcase u aktivitija");
            Log.d(TAG, "setCase: " + slucaj.equals(null));
            Bundle bn = new Bundle();
            bn.putParcelable("slucaj_objekat", slucaj);
            bottomfrag.getCase(bn);
        } else {
            FragmentForDynamicEditText fragment = new FragmentForDynamicEditText();
            Bundle bundle = new Bundle();
            bundle.putParcelable("slucaj_objekat", slucaj);

            fragment.getCase(bundle);

//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.dymanic_fragment_container, fragment)
//                    .addToBackStack(null)
//                    .commit();

        }
//
//        Intent intent = new Intent(AddSlucaj.this, AddSlucaj.class);
//
//        intent.putExtra("slucaj_objekat", slucaj);
//        startActivity(intent);
//
//
//        Log.d(TAG, "setCase: slucaj je sad u mejnu" + slucaj.getBroj_slucaja());
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("slucaj_objekat", slucaj);
//
//        FragmentForDynamicEditText fragmentForDynamicEditText = new FragmentForDynamicEditText();
//        fragmentForDynamicEditText.getCase(bundle);
    }
}
