package com.example.berka.advokatormlite.activities.add_points;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.berka.advokatormlite.activities.main.MainActivityContractMVP;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.example.berka.advokatormlite.activities.main.MainActivity.TAG;

/**
 * Created by berka on 12-Mar-18.
 */

public class PronadjeniPresenter implements PronadjeniContractMVP.Presenter {

    @Nullable
    private PronadjeniContractMVP.View view;
    private PronadjeniContractMVP.Model model;


    private double vrednostJednogBodaUDinarima = 30;

    public PronadjeniPresenter(PronadjeniContractMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(PronadjeniContractMVP.View view) {
        this.view = view;
    }

    @Override
    public void loadExpandableListViewData(Slucaj slucaj) {
        HashMap<Tarifa, List<Radnja>> listHashMap = null;
        List<Tarifa> listViewHeaders;
            if (!slucaj.equals(null)) {
                if (slucaj.getPostupak().getNazivpostupka().equals("Krivicni postupak")) {
                    listViewHeaders = model.getHeadersKrivica(slucaj.getPostupak());
                    if (!listViewHeaders.isEmpty()) {
                        listHashMap = model.getHashMapKrivica(listViewHeaders);
                    }
                } else {
                    listViewHeaders = model.getHeadersNonKrivica(slucaj.getPostupak());

                    if (!listViewHeaders.isEmpty()) {
                        listHashMap = model.getHashMapNonKrivica(listViewHeaders, slucaj.getPostupak());
                    }
                }

                if (!listHashMap.isEmpty()) {
                    view.populateExpandableListView(listViewHeaders, listHashMap);
                }

            } else {
                view.showSomeError();
            }
    }

    @Override
    public void radnjaItemHasBeenClicked(Radnja radnja, Slucaj slucaj) {

        double privremenaCena;

        if (view != null) {
            //sifra 1: puna vrednost, 2: pola, 3: duplo, 4: vrednost i 50 bodova po satu, 5: puna vrednost i broj sati moze biti i pola ako se ne odrzi
            switch (radnja.getSifra()) {

                case 1:
                    //metoda koja racuna pravu vrednost i ima sifru 1
                    privremenaCena = wholeValue(slucaj);
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 2:
                    //metoda koja racuna polovinu vrednosti ima sifru 2
                    privremenaCena = halfValue(slucaj);
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 3:
                    //metoda koja racuna duplu vrednost ima sifru 3
                    privremenaCena = doubleValue(slucaj);
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 4:
                    //metoda koja racuna pravu vrednost plus broj sati ima sifru 4
                    privremenaCena = wholePlusHours(slucaj);
                    view.dialogFixValuePlusHours(privremenaCena);
                    break;
                default:
            }
        }
    }

    @Override
    public void buttonclicked(){
        view.showSomeError();
    }

    @Override
    public void addRadnjaButtonClicked(double cenaRadnje, String imeRadnje, Slucaj slucaj) {


        if (cenaRadnje != 0 && !imeRadnje.equals(null) && !slucaj.equals(null)) {
            model.saveRadnja(cenaRadnje, imeRadnje, slucaj);
            view.radnjaAddedMessage();
        } else {
            view.cantAddRadnjaShowMessage();
        }
    }

    private double wholeValue(Slucaj slucaj) {
        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izrecunataVrednostUBodovima = cena + ((cena / 2) * (slucaj.getBroj_stranaka() - 1));
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double halfValue(Slucaj slucaj) {

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izrecunataVrednostUBodovima = (cena + ((cena / 2) * (slucaj.getBroj_stranaka() - 1))) / 2;
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double doubleValue(Slucaj slucaj) {

        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izrecunataVrednostUBodovima = (cena + ((cena / 2) * (slucaj.getBroj_stranaka() - 1))) * 2;
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private double wholePlusHours(Slucaj slucaj) {

        int cena = slucaj.getTabelaBodova().getBodovi();
        double izrecunataVrednostUBodovima = cena + ((cena / 2) * (slucaj.getBroj_stranaka() - 1));
        return izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
    }

    private String getNowDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String datestring = sdf.format(c.getTime());
        return datestring;
    }
}
