package com.example.berka.advokatormlite.activities.add_case;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.BaseFragment;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentNavigation;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForKrivica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentForParnica;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentPrekrsaj;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentUstavniSud;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

/**
 * Created by berka on 22-Feb-18.
 */

public class AddSlucajPresenter implements AddSlucajMVP.Presenter{

    private static final String TAG = "AddSlucajPresenter";
    @Nullable
    private AddSlucajMVP.View view;
    private AddSlucajMVP.Model model;

    public AddSlucajPresenter(AddSlucajMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(AddSlucajMVP.View view) {
        this.view = view;
    }
}
