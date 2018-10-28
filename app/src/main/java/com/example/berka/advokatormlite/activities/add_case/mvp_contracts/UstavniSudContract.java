package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.UpperFragmentUstavniSud;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface UstavniSudContract {

    interface View{

        void addSlucaj();

        void emptyCasePasswordWarning();

        void numbersOnlyTestingWarning();

        void thisCaseAlreadyExists();

        void caseAddedMessage();

        void loadTabelaBodovaVrednost(TabelaBodova fiksniBodoviUstavniSud);
    }

    interface Presenter extends BaseContract.Presenter<View>{

        void setView(View view);

        void detachView();

        void getVrednostiTabeleBodova(Postupak postupak);

        Slucaj saveCaseButtonClicked(int i, Postupak postupak, TabelaBodova tabelaBodova, int broj_stranaka);
    }

    interface Model{

        TabelaBodova getZapreceneKazne(Postupak postupak);
    }
}
