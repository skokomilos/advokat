package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import android.os.Bundle;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

/**
 * Created by berka on 23-Apr-18.
 */

public interface BottomFragmentContract {

    interface View{

        void getCase(Bundle bundle);

        void addRedniBrojStranke(int redni_broj);

        void addEditTextZaImeStraneke();

        void addEditTextZaAdresu();

        void addEditTextZaMesto();

        String getClientName(int redni_broj_edit_texta_u_grid_layout);

        String getClientAddress(int i);

        String getClientPlace(int i);

        void errorMessage();

        void caseAddedToDataBaseMessage();

        void goToPronadjeniSlucajActivity();
    }

    interface Presenter{

        void addEditTextsForClient(int broj_stranaka);

        void saveCaseDataAndClientsData(Slucaj slucaj);

        void setView(BottomFragmentContract.View view);

        void detachView();

        void tjestMetod(String cao);
    }

    interface Model{

        void saveSlucaj(Slucaj slucaj);

        void saveStrankaDetails(StrankaDetail strankaDetail);
    }
}
