package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 07-Jun-18.
 */

public interface PrekrsajRepositoryInterface {

    List<TabelaBodova> queryForAllTabelaBodova(Postupak postupak);
}
