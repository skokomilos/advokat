package com.example.berka.advokatormlite.activities.add_points;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 12-Mar-18.
 */

public interface PronadjeniContractMVP {

    interface View{

        void goToKonacniRacunButtonClicked();

        void populateExpandableListView(List<Tarifa> listViewHeaders, HashMap<Tarifa, List<Radnja>> listHashMap);

        void showSomeError();

        void openDialog(double privremenaCena, String naziv_radnje);

        void dialogFixValuePlusHours(double privremenaCena);

        void cantAddRadnjaShowMessage();

        void radnjaAddedMessage();

    }

    interface Presenter{

        void setView(PronadjeniContractMVP.View view);

        void loadExpandableListViewData(Slucaj slucaj);

        void radnjaItemHasBeenClicked(Radnja radnja, Slucaj slucaj);

        void buttonclicked();

        void addRadnjaButtonClicked(double cenaRadnje, String imeRadnje, Slucaj slucaj);
    }

    interface Model{

        HashMap<Tarifa, List<Radnja>> getHashMapKrivica(List<Tarifa> listViewHeaders);

        HashMap<Tarifa, List<Radnja>> getHashMapNonKrivica(List<Tarifa> listViewHeaders, Postupak postupak);

        List<Tarifa> getHeadersKrivica(Postupak postupak);

        List<Tarifa> getHeadersNonKrivica(Postupak postupak);

        void saveRadnja(double cenaRadnje, String imeRadnje, Slucaj slucaj);
    }
}
