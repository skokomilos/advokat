package com.example.berka.advokatormlite.interfaces;

import com.example.berka.advokatormlite.model.StrankaDetail;

import java.util.ArrayList;

/**
 * Created by berka on 18-Jan-18.
 */

public interface IDynamicallyAddEditTexts {


     void addEditTextsProgramabilno();
     void initPostupak();
     ArrayList<StrankaDetail> allStrankeFromListViewToArrayList();
     StrankaDetail getVrednostiSvihPoljaZaStranku(int position);

}
