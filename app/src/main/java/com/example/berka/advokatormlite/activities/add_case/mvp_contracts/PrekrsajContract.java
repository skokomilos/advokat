package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentPrekrsaj;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by berka on 18-Apr-18.
 */

public interface PrekrsajContract{

    interface View extends BaseContract.View<Presenter>{

    }

    interface Presenter extends BaseContract.Presenter<View>{

        void getVrednostiTabeleBodova(Postupak postupak);
    }

    interface Model{

        List<TabelaBodova> getZapreceneKazne(Postupak postupak);
    }
}
