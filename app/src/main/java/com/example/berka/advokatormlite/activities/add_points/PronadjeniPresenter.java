package com.example.berka.advokatormlite.activities.add_points;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.Tarifa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 12-Mar-18.
 */

public class PronadjeniPresenter implements PronadjeniContractMVP.Presenter {

    private static final String TAG = "PronadjeniPresenter";
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
            if (slucaj.getPostupak().getNazivpostupka().equals("Krivični postupak")) {
                listViewHeaders = model.getHeadersKrivica(slucaj.getPostupak());
                if (!listViewHeaders.isEmpty()) {
                    listHashMap = model.getHashMapKrivica(listViewHeaders);
                }
            } else {
                listViewHeaders = model.getHeadersNonKrivica(slucaj.getPostupak());
                Log.d(TAG, "Parnica je ili  nesto drugo" + listViewHeaders.get(0).getNaslov_tarife());

                if (!listViewHeaders.isEmpty()) {
                    Log.d(TAG, "loadExpandableListViewData:" + listViewHeaders.size() + " " + slucaj.getBroj_slucaja());
                    listHashMap = model.getHashMapNonKrivica(listViewHeaders, slucaj.getPostupak());
                }
            }

            if (!listHashMap.isEmpty()) {
                Log.d(TAG, "loadExpandableListViewData: HASH nije prejzan  ");
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
//                    if(slucaj.getVrsta_odbrane()==1){
//                        privremenaCena = privremenaCena/2;
//                    }
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 2:
                    //metoda koja racuna polovinu vrednosti ima sifru 2
                    privremenaCena = halfValue(slucaj);
                    if(slucaj.getVrsta_odbrane()==1){
                        privremenaCena = privremenaCena/2;
                    }
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 3:
                    //metoda koja racuna duplu vrednost ima sifru 3
                    privremenaCena = doubleValue(slucaj);
                    view.openDialog(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 4:
                    //metoda koja racuna pravu vrednost plus broj sati i koja moze biti otkazana ima sifru 4
                    privremenaCena = wholePlusHours(slucaj);
                    if(slucaj.getVrsta_odbrane()==1){
                        privremenaCena = privremenaCena/2;
                    }
                    view.openCancelableCaseDialogWithHours(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 5:
                    //metoda koja racuna celu vrednost plus broj sati i koja nema opciju otkazano ima sifru 5
                    privremenaCena = wholePlusHours(slucaj);
                    if(slucaj.getVrsta_odbrane()==1){
                        privremenaCena = privremenaCena/2;
                    }
                    view.openDialogWithHours(privremenaCena, radnja.getNaziv_radnje());
                    break;
                case 6:
                    //metoda koja racuna pola vrednosti plus sati i nema opciju otkazano
                    privremenaCena = halfValue(slucaj);
                    if(slucaj.getVrsta_odbrane()==1){
                        privremenaCena = privremenaCena/2;
                    }
                    view.openDialogWithHours(privremenaCena, radnja.getNaziv_radnje());
                default:
            }
        }
    }

    /**
     *
     * @param slucaj
     * @param cena
     * @return
     *
     *kad je okrivljenI(sifra 0) odbrana moze biti po punomocju(puna vrednost) i po s. duznosti(pola vrednosti), kad je ostecen(sifra 1) onda je isto pola vrednosti
     */
    private double proveri_vrstu_odbrane(Slucaj slucaj, double cena){
        if(slucaj.getOkrivljen()==0){
            if(slucaj.getVrsta_odbrane()==1){
                cena = cena/2;
            }
        }else {
            cena = cena/2;
        }
        return cena;
    }

    @Override
    public void buttonclicked() {
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

    @Override
    public void loadAllParties(Slucaj slucaj) {

        List<StrankaDetail> sveStrankeSlucaja = null;
        if (slucaj != null) {
            sveStrankeSlucaja = model.getAllPartiesOfCase(slucaj);
        }
        if (view != null) {
            view.showPartiesInDialog(sveStrankeSlucaja);
        }
    }

    @Override
    public void loadAllPartiesForChange(Slucaj slucaj) {

        List<StrankaDetail> sveStrankeSlucaja = null;
        if (slucaj != null) {
            sveStrankeSlucaja = model.getAllPartiesOfCase(slucaj);
        }

        if (view != null) {
            view.showEditPartiesDialog(sveStrankeSlucaja);
        }
    }

    @Override
    public void editParties(List<StrankaDetail> sveStrankeSlucaja) {

        for (int i = 0; i < sveStrankeSlucaja.size(); i++) {
            model.setStrankaDetail(sveStrankeSlucaja.get(i));
        }
    }


    private double wholeValue(Slucaj slucaj) {
        double cena = Double.valueOf(slucaj.getTabelaBodova().getBodovi());
        double izracunataCena;
        double izrecunataVrednostUBodovima = cena + ((cena / 2) * (slucaj.getBroj_stranaka() - 1));
        izracunataCena =  izrecunataVrednostUBodovima * vrednostJednogBodaUDinarima;
        return proveri_vrstu_odbrane(slucaj, izracunataCena);
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
