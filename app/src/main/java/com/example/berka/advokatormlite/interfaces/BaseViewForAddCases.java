package com.example.berka.advokatormlite.interfaces;

import android.widget.GridLayout;

import com.example.berka.advokatormlite.model.Slucaj;

/**
 * Created by berka on 15-Apr-18.
 */

public interface BaseViewForAddCases{

    void addSlucaj();

    void goToPronadjeniSlucajAcitivty(Slucaj slucaj);

    void emptyCasePasswordWarning();

    void noCasePasswordWarning();

    void caseExistsWarning();

    void caseAddesMessage();
}
