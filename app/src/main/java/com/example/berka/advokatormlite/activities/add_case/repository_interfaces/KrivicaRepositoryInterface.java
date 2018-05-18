package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface KrivicaRepositoryInterface {

    void insertObjectSlucaj(Slucaj slucaj);


    List<TabelaBodova> queryForAllTabelaBodova(Postupak postupak);
}
