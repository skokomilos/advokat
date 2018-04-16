package com.example.berka.advokatormlite.activities.main;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 06-Mar-18.
 */

public interface MainActivityRespository {

    List<Slucaj> queryForAllCases();

    Slucaj findCaseById(String slucajId);

    List<Postupak> queryForAllPostupci();

}
