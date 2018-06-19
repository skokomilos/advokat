package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import android.content.Context;
import android.os.Bundle;

import com.example.berka.advokatormlite.activities.add_case.presenters.BasePresenter;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.sql.SQLException;

/**
 * Created by berka on 23-Apr-18.
 */

public interface BottomFragmentContract {

    interface View extends BaseView<Presenter>{

        void getCase(Bundle bundle);

        void addRedniBrojStranke(int redni_broj);

        void addEditTextZaImeStraneke();

        void addEditTextZaAdresu();

        void addEditTextZaMesto();

        String getClientName(int redni_broj_edit_texta_u_grid_layout);

        String getClientAddress(int i);

        String getClientPlace(int i);

        void errorMessage(String string);

        void caseAddedToDataBaseMessage();

        void goToPronadjeniSlucajActivity();

        boolean isActive();

        void onShowToast(String msg);

        FragmentForDynamicEditText getActivityContext();

    }

    interface Presenter extends BasePresenter<View>{

        void addEditTextsForClient(int broj_stranaka);

        void saveCaseDataAndClientsData(Slucaj slucaj);

        void loadStatistics();

        void setView(BottomFragmentContract.View view);

        void detachView();
    }

    interface Model{

        boolean saveSlucaj(Slucaj slucaj);

        void saveStrankaDetails(StrankaDetail strankaDetail);
    }

    interface RequiredModelOps {

        void sameCaseNumberError(String string);
    }

    interface RequiredPresenterOps{

        void sameCaseNumberError(String string);
    }

    interface requiredViewOps extends ActivityView{

        public void finalDest(String string);


    }
}
