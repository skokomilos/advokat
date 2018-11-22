package com.example.berka.advokatormlite.activities.ustavni;

import android.support.design.widget.TabLayout;

import com.example.berka.advokatormlite.interfaces.IAddCasePresenter;
import com.example.berka.advokatormlite.interfaces.IAddCaseView;
import com.example.berka.advokatormlite.model.TabelaBodova;

/**
 * Created by berka on 26-Feb-18.
 */

public interface AddUstavniContract {
    interface View extends IAddCaseView {

    }

    interface Presenter extends IAddCasePresenter {
        TabelaBodova getSpecificObjectFromTabelaBodova(int id);
    }
}
