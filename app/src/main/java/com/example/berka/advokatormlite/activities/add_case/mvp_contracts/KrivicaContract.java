package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface KrivicaContract {

    interface View extends BaseContract.View<Presenter>{

    }

    interface Presenter extends BaseContract.Presenter<View>{

        int radioButtonCurrentValue(String radiovalue);

        void getVrednostiTabeleBodova(Postupak postupak);
    }

    interface Model{

        List<TabelaBodova> getZapreceneKazne(Postupak postupak);
    }
}
