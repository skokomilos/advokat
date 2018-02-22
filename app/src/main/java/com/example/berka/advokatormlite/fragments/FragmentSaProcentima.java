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
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by berka on 19-Jan-18.
 */

public class FragmentSaProcentima extends Fragment {


    View view;

    private Unbinder unbinder;

    @BindView(R.id.numberPicker)
    NumberPicker picker;

    @BindView(R.id.izracunaj_ispravu_procenti)
    Button button;

    @BindView(R.id.vrednost_spora_isprave)
    EditText editText;

    double choosenNum;

    public static final String TAG = "TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sa_procentima, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chooseNumberFromNumberPicker();
    }

    public void chooseNumberFromNumberPicker(){


        picker.setMinValue(1);
        picker.setMaxValue(8);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues( new String[] {"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8"} );
        double db = picker.getValue()/10.0;

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal1, int newVal1) {

                choosenNum = picker.getValue()/10.0;
                Log.d(TAG, "onValueChange: " + picker.getValue());
            }
        });

    }

    @OnClick(R.id.izracunaj_ispravu_procenti)
    public void dodajIspravu(){

        Toast.makeText(getActivity(), "Ovo izgleda radi  " + editText.getText().toString() + " " + choosenNum, Toast.LENGTH_LONG).show();
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
