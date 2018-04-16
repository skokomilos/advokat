package com.example.berka.advokatormlite.activities.add_points;

import android.util.Log;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 12-Mar-18.
 */

public class PronadjeniModel implements PronadjeniContractMVP.Model {

    PronadjeniRepository repository;

    public PronadjeniModel(PronadjeniRepository repository) {
        this.repository = repository;
    }

    @Override
    public HashMap<Tarifa, List<Radnja>> getHashMapKrivica(List<Tarifa> listViewHeaders) {


        HashMap<Tarifa, List<Radnja>> listHashMap = new HashMap<>();
        List<Radnja> listaRadnji;

        for (int i = 0; i < listViewHeaders.size(); i++) {

            listaRadnji = new ArrayList<>(listViewHeaders.get(i).getRadnje());
            listHashMap.put(listViewHeaders.get(i), listaRadnji);
        }

        return listHashMap;
    }

    @Override
    public HashMap<Tarifa, List<Radnja>> getHashMapNonKrivica(List<Tarifa> listViewHeaders, Postupak postupak) {
        return repository.queryForNonKrivicaHashMap(listViewHeaders, postupak);
    }

    @Override
    public List<Tarifa> getHeadersKrivica(Postupak postupak) {
        return repository.queryForKrivicaHeaders(postupak);
    }

    @Override
    public List<Tarifa> getHeadersNonKrivica(Postupak postupak) {
        return repository.queryForNonKrivicaHeaders(postupak);
    }

    @Override
    public void saveRadnja(double cenaRadnje, String imeRadnje, Slucaj slucaj) {
        repository.insertRadnja(cenaRadnje, imeRadnje, slucaj);
    }
}
