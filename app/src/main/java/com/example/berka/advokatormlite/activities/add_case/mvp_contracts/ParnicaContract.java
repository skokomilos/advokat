package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface ParnicaContract {

    interface View{
        void loadSpinnerProcenjivoNeprocenjivo(List<VrsteParnica> vrstaParnica);

        void loadSpinnerTabelaBodova(List<TabelaBodova> tabelaBodova);

        void AddSlucaj();

//        void loadSpinnerPromenljivoNepromenljivo(List<VrsteParnica> vrstaParnica);
//
//        void loadSpinner(List<TabelaBodova> tabelaBodova);
    }

    interface Presenter extends BaseContract.Presenter<View>{

        void getVrsteParnica_ProcenjivoNeprocenjivo(Postupak postupak);

        void getVrednostiTabeleBodovaZaOvajPostupak(VrsteParnica vrsteParnica, Postupak postupak);

        //Slucaj saveCaseButtonClicked(int broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int broj_stranaka);

        void welcomeMessage();
    }

    interface Model{

        List<VrsteParnica> loadZapreceneKazne(Postupak postupak) throws SQLException;

        List<TabelaBodova> get_Unikatne_Neprocenjive_Vrednosti_Iz_TabeleBodova(VrsteParnica vrsteParnica, Postupak postupak);

        List<TabelaBodova> get_Neprocenjive_Vrednosti_TabeleBodova_Koje_Nisu_Unikatne(VrsteParnica vrsteParnica);

        List<TabelaBodova> get_Procenjene_Vrednosti_TabelaBodova(VrsteParnica vrsteParnica);
    }
}
