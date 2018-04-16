package com.example.berka.advokatormlite.interfaces;

import android.widget.Spinner;

import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 24-Feb-18.
 */

public interface IAddCaseView {
     int getPostupakIdFromMainActivityIntent();
     int getBrojStranakaFromMainActivityIntent();
     void addEditTextsProgramabilno();
     StrankaDetail getDetailValuesForSingleClient(int position);
     ArrayList<StrankaDetail> allClientsDetailValues();
     void loadSpinner(List<TabelaBodova> sveZapreceneKazne);
     void gotoFoundCaseActivity(int caseId);
     String getCasePasswordFromEditText();
     TabelaBodova getSelectedZaprecenaKazna(Spinner spinner);
     //int okrivljenOstecenValue();
     void emptyCasePasswordWarning();
     void numbersOnlyTestingWarning();
     void caseWithThisCaseAlreadyExists();
     void caseAddedMessage(Slucaj slucaj);
}
