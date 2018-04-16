package com.example.berka.advokatormlite.activities.main;

import android.util.Log;

import com.example.berka.advokatormlite.data.prefs.PreferencesHelper;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by berka on 28-Feb-18.
 */

public class MainActivityModel implements MainActivityContractMVP.Model {

    private static final String TAG = "MainActivityModel";
    private MainActivityRespository repository;
    private final PreferencesHelper mPreferenceHelper;

    public MainActivityModel(MainActivityRespository repository, PreferencesHelper mPreferenceHelper) {
        this.repository = repository;
        this.mPreferenceHelper = mPreferenceHelper;
    }

    @Override
    public Slucaj getSlucaj(String caseId) {

        return repository.findCaseById(caseId);
    }

    @Override
    public List<Postupak> getPostupci() {

       return repository.queryForAllPostupci();
    }

    @Override
    public List<Slucaj> getSlucajevi() {

        return repository.queryForAllCases();
    }

    public PreferencesHelper getPreferencesHelper(){
        return mPreferenceHelper;
    }

    @Override
    public void updateUserInfo(String userName, String address, String place) {

        mPreferenceHelper.setUserNameAddressPlace(userName, address, place);
    }

    @Override
    public String getCurentUserName() {

       return mPreferenceHelper.getCurrentUserName();
    }

    @Override
    public String getCurrentAddress() {

        return mPreferenceHelper.getCurrentAddress();
    }

    @Override
    public String getCurretnPlace() {

        return mPreferenceHelper.getCurrentPlace();
    }
}
