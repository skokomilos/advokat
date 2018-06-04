package com.example.berka.advokatormlite.activities.add_points;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
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

        void showEditPartiesDialog(List<StrankaDetail> sveStrankeSlucaja);

        void openDialog(double privremenaCena, String naziv_radnje);

        void dialogFixValuePlusHours(double privremenaCena, String nazivRadnje);

        void cantAddRadnjaShowMessage();

        void radnjaAddedMessage();

        void showPartiesInDialog(List<StrankaDetail> sveStrankeSlucaja);

    }

    interface Presenter{

        void setView(PronadjeniContractMVP.View view);

        void loadExpandableListViewData(Slucaj slucaj);

        void radnjaItemHasBeenClicked(Radnja radnja, Slucaj slucaj);

        void buttonclicked();

        void addRadnjaButtonClicked(double cenaRadnje, String imeRadnje, Slucaj slucaj);

        void loadAllParties(Slucaj slucaj);

        void loadAllPartiesForChange(Slucaj slucaj);

        void editParties(List<StrankaDetail> sveStrankeSlucaja);
    }

    interface Model{

        HashMap<Tarifa, List<Radnja>> getHashMapKrivica(List<Tarifa> listViewHeaders);

        HashMap<Tarifa, List<Radnja>> getHashMapNonKrivica(List<Tarifa> listViewHeaders, Postupak postupak);

        List<Tarifa> getHeadersKrivica(Postupak postupak);

        List<Tarifa> getHeadersNonKrivica(Postupak postupak);

        void saveRadnja(double cenaRadnje, String imeRadnje, Slucaj slucaj);

        List<StrankaDetail> getAllPartiesOfCase(Slucaj slucaj);

        void setStrankaDetail(StrankaDetail strankaDetail);
    }
}
