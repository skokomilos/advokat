package com.example.berka.advokatormlite.activities.add_points;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.HashMap;
import java.util.List;

import dagger.Provides;

/**
 * Created by berka on 12-Mar-18.
 */

public interface PronadjeniRepository {

    void insertRadnja(double cenaRadnje, String naziv_radnje, Slucaj slucaj);

    List<Tarifa> queryForKrivicaHeaders(Postupak postupak);

    List<Tarifa> queryForNonKrivicaHeaders(Postupak postupak);

    HashMap<Tarifa, List<Radnja>> queryForNonKrivicaHashMap(List<Tarifa> headers, Postupak postupak);
}
